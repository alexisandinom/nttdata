package com.nttdata.customerservice.config;

import com.nttdata.common.events.CustomerCreatedEvent;
import com.nttdata.common.events.CustomerDeletedEvent;
import com.nttdata.common.events.CustomerUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private static final String CUSTOMER_CREATED_TOPIC = "customer.created";
    private static final String CUSTOMER_UPDATED_TOPIC = "customer.updated";
    private static final String CUSTOMER_DELETED_TOPIC = "customer.deleted";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCustomerCreated(CustomerCreatedEvent event) {
        try {
            kafkaTemplate.send(CUSTOMER_CREATED_TOPIC, event.getCustomerId(), event);
            logger.info("Published CustomerCreatedEvent to topic: {}", CUSTOMER_CREATED_TOPIC);
        } catch (Exception e) {
            logger.error("Error publishing CustomerCreatedEvent: {}", e.getMessage(), e);
        }
    }

    public void publishCustomerUpdated(CustomerUpdatedEvent event) {
        try {
            kafkaTemplate.send(CUSTOMER_UPDATED_TOPIC, event.getCustomerId(), event);
            logger.info("Published CustomerUpdatedEvent to topic: {}", CUSTOMER_UPDATED_TOPIC);
        } catch (Exception e) {
            logger.error("Error publishing CustomerUpdatedEvent: {}", e.getMessage(), e);
        }
    }

    public void publishCustomerDeleted(CustomerDeletedEvent event) {
        try {
            kafkaTemplate.send(CUSTOMER_DELETED_TOPIC, event.getCustomerId(), event);
            logger.info("Published CustomerDeletedEvent to topic: {}", CUSTOMER_DELETED_TOPIC);
        } catch (Exception e) {
            logger.error("Error publishing CustomerDeletedEvent: {}", e.getMessage(), e);
        }
    }
}



