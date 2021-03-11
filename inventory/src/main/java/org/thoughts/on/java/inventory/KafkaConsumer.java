package org.thoughts.on.java.inventory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaConsumer {

    private final Logger logger = Logger.getLogger(KafkaConsumer.class.getSimpleName());

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    private InventoryRepository inventoryRepo;

    private InventoryReservationRepository inventoryReservationRepo;

    private OutboxUtil outboxUtil;

    public KafkaConsumer(InventoryRepository inventoryRepo, InventoryReservationRepository inventoryReservationRepo, OutboxUtil outboxUtil) {
        this.inventoryRepo = inventoryRepo;
        this.inventoryReservationRepo = inventoryReservationRepo;
        this.outboxUtil = outboxUtil;
    }

    @Transactional
    @KafkaListener(topics = "outbox.event.SagaOrder", groupId = "inventory")
    public void consume(String event) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", event.toString()));

        JsonObject json = Json.createReader(new StringReader(event)).readObject();
        JsonObject payload = json.containsKey("schema") ? json.getJsonObject("payload") : json;

        SagaOperation eventType = SagaOperation.valueOf(payload.getString("eventType"));
        Long eventId = Long.valueOf(payload.getString("id"));

        if (eventType == SagaOperation.RESERVE_INVENTORY) {
            String eventPayload = payload.getString("payload");

            List<InventoryReservation> rs = inventoryReservationRepo.getByOrderId(eventId);
            if (!rs.isEmpty()) {
                logger.info("Remove existing reservations for order");
                removeReservations(rs);
            }

            JsonReader payloadReader = Json.createReader(new StringReader(eventPayload));
            JsonObject payloadObject = payloadReader.readObject();

            JsonArray positions = payloadObject.getJsonArray("positions");
            List<OrderPosition> orderPositions = positions.getValuesAs(OrderPosition::new);

            SagaOperation op = SagaOperation.INVENTORY_RESERVED;
            for (OrderPosition p : orderPositions) {
                Inventory i = inventoryRepo.findByBookId(p.getBookId());
                if (i == null || i.getQuantity() < p.getQuantity()) {
                    op = SagaOperation.INSUFFICIENT_INVENTORY;
                    logger.info("Insufficient inventory");
                }
            }
            if (op == SagaOperation.INVENTORY_RESERVED) {
                logger.info("Reserve inventory");
                for (OrderPosition p : orderPositions) {
                    Inventory i = inventoryRepo.findByBookId(p.getBookId());
                    i.setQuantity(i.getQuantity() - p.getQuantity());

                    InventoryReservation r = new InventoryReservation();
                    r.setOrderId(eventId);
                    r.setBookId(p.getBookId());
                    r.setQuantity(p.getQuantity());
                    inventoryReservationRepo.save(r);
                }
            }

            if (op == SagaOperation.INVENTORY_RESERVED) {
                outboxUtil.writeEventToOutbox(eventId, Operation.Prepared);
            } else {
                outboxUtil.writeEventToOutbox(eventId, Operation.Rejected);
            }
            outboxUtil.writeSagaEventToOutbox(eventId, op);
        } else if (eventType == SagaOperation.RELEASE_INVENTORY) {
            removeReservations(inventoryReservationRepo.getByOrderId(eventId));
            outboxUtil.writeSagaEventToOutbox(eventId, SagaOperation.INVENTORY_RELEASED);
        }
    }

    private void removeReservations(List<InventoryReservation> rs) {
        for (InventoryReservation r : rs) {
            Inventory i = inventoryRepo.findByBookId(r.getBookId());
            i.setQuantity(i.getQuantity()+r.getQuantity());
            inventoryReservationRepo.delete(r);
        }
    }
}