package org.thoughts.on.java.order;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
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

    @Autowired
    private OrderInfoRepository orderRepo;

    @Autowired
    private BookClient bookClient;

    @Transactional
    @KafkaListener(topics = "outbox.event.Inventory", groupId = "orderInfo")
    public void consumeInventoryUpdates(String event) throws IOException {
        logger.info(String.format("#### -> Consumed inventory event -> %s", event.toString()));

        JsonObject json = Json.createReader(new StringReader(event)).readObject();
        JsonObject payload = json.containsKey("schema") ? json.getJsonObject("payload") : json;

        String eventType = payload.getString("eventType");
        Long eventId = Long.valueOf(payload.getString("id"));

        OrderInfo order = orderRepo.findById(eventId).get();
        if (eventType.equals("Prepared")) {
            for (OrderPosition pos : order.getPositions()) {
                pos.setAvailable(true);
            }
            logger.info("OrderPositions of Order with id ["+eventId+"] are available");
        } else {
            for (OrderPosition pos : order.getPositions()) {
                pos.setAvailable(false);
            }
            logger.info("OrderPositions of Order with id ["+eventId+"] are unavailable");
        }
    }

    @Transactional
    @KafkaListener(topics = "outbox.event.Order", groupId = "orderInfo")
    public void consumeOrderUpdates(String event) throws IOException {
        logger.info(String.format("#### -> Consumed order event -> %s", event.toString()));

        JsonObject json = Json.createReader(new StringReader(event)).readObject();
        JsonObject payload = json.containsKey("schema") ? json.getJsonObject("payload") : json;

        String eventType = payload.getString("eventType");
        Long eventId = Long.valueOf(payload.getString("id"));

        if (eventType.equals("CREATED")) {
            String eventPayload = payload.getString("payload");
            JsonReader payloadReader = Json.createReader(new StringReader(eventPayload));
            JsonObject payloadObject = payloadReader.readObject();
            
            OrderInfo info = new OrderInfo();
            info.setId(eventId);
            info.setCustomerName(payloadObject.getString("customer"));
            info.setState(OrderState.PENDING);
           
            JsonArray positions = payloadObject.getJsonArray("positions");
            Set<OrderPosition> orderPositions = new HashSet<OrderPosition>(positions.getValuesAs(OrderPosition::new));
            for (OrderPosition o : orderPositions) {
                o.setBook(bookClient.getBook(o.getBookId()).getTitle());
                o.setOrder(info);
            }
            info.setPositions(orderPositions);

            orderRepo.save(info);
            logger.info("Added info about order with id ["+info.getId()+"]");
        } else if (eventType.equals("CONFIRMED")) {
            OrderInfo info = orderRepo.findById(eventId).get();
            info.setState(OrderState.CONFIRMED);
            logger.info("Order with id ["+info.getId()+"] got confirmed");
        }
        else if (eventType.equals("REJECTED")) {
            OrderInfo info = orderRepo.findById(eventId).get();
            info.setState(OrderState.REJECTED);
            logger.info("Order with id ["+info.getId()+"] got rejected");
        }
    }

}