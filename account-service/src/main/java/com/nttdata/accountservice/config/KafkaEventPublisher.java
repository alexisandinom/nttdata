package com.nttdata.accountservice.config;

import com.nttdata.common.events.AccountCreatedEvent;
import com.nttdata.common.events.MovementCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private static final String ACCOUNT_CREATED_TOPIC = "account.created";
    private static final String MOVEMENT_CREATED_TOPIC = "movement.created";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAccountCreated(AccountCreatedEvent event) {
        try {
            kafkaTemplate.send(ACCOUNT_CREATED_TOPIC, event.getAccountId(), event);
            logger.info("Published AccountCreatedEvent to topic: {}", ACCOUNT_CREATED_TOPIC);
        } catch (Exception e) {
            logger.error("Error publishing AccountCreatedEvent: {}", e.getMessage(), e);
        }
    }

    public void publishMovementCreated(MovementCreatedEvent event) {
        try {
            kafkaTemplate.send(MOVEMENT_CREATED_TOPIC, event.getMovementId(), event);
            logger.info("Published MovementCreatedEvent to topic: {}", MOVEMENT_CREATED_TOPIC);
        } catch (Exception e) {
            logger.error("Error publishing MovementCreatedEvent: {}", e.getMessage(), e);
        }
    }
}



