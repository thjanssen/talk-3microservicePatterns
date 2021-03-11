package org.thoughts.on.java.order;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaConsumer {

    private final Logger logger = Logger.getLogger(KafkaConsumer.class.getSimpleName());

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    private Orchestrator orchestrator;

    public KafkaConsumer(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @Transactional
    @KafkaListener(topics = "outbox.event.SagaOrder", groupId = "order")
    public void consume(String event) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", event.toString()));

        JsonObject json = Json.createReader(new StringReader(event)).readObject();
        JsonObject payload = json.containsKey("schema") ? json.getJsonObject("payload") :json;

        SagaOperation op = SagaOperation.valueOf(payload.getString("eventType"));
        // ignore events that we send ourself
        if (op == SagaOperation.RESERVE_INVENTORY 
            || op == SagaOperation.RELEASE_INVENTORY
            || op == SagaOperation.PROCESS_PAYMENT) {
            return;
        }

        Long orderId = Long.valueOf(payload.getString("id"));
        
        switch (op) {
            case INVENTORY_RESERVED:
                orchestrator.inventoryReserved(orderId);
                break;

            case INSUFFICIENT_INVENTORY:
                orchestrator.insufficientInventory(orderId);
                break;

            case PAYMENT_SUCCESSFUL:
                orchestrator.paymentSuccessful(orderId);
                break;

            case PAYMENT_FAILED:
                orchestrator.paymentFailed(orderId);
                break;

            case INVENTORY_RELEASED:
                orchestrator.inventoryReleased(orderId);
                break;

            default:
                throw new IllegalArgumentException("Unsupported SagaOperation: "+op);
        }
    }

}