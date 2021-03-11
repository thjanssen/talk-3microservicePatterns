package org.thoughts.on.java.payment;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.json.Json;
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
    private OutboxUtil outboxUtil;

    @Transactional
    @KafkaListener(topics = "outbox.event.SagaOrder", groupId = "payment")
    public void consume(String event) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", event.toString()));

        JsonObject json = Json.createReader(new StringReader(event)).readObject();
        JsonObject payload = json.containsKey("schema") ? json.getJsonObject("payload") :json;

        String eventType = payload.getString("eventType");
        Long eventId = Long.valueOf(payload.getString("id"));

        if (eventType.equals("PROCESS_PAYMENT")) {
            String eventPayload = payload.getString("payload");

            JsonReader payloadReader = Json.createReader(new StringReader(eventPayload));
            JsonObject payloadObject = payloadReader.readObject();
            Double amount = payloadObject.getJsonNumber("amount").doubleValue();

            if (amount < 100.0) {
                logger.info("Successfully processed payment for order "+eventId);
                
                outboxUtil.writeToOutbox(eventId, Operation.PAYMENT_SUCCESSFUL);
            } else {
                logger.info("Failed payment for order "+eventId);
                
                outboxUtil.writeToOutbox(eventId, Operation.PAYMENT_FAILED);
            }
        } else {
            logger.info("Ignore event of type "+eventType);
        }
    }

}