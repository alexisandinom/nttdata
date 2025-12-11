package com.nttdata.accountservice.config;

import com.nttdata.common.events.CustomerCreatedEvent;
import com.nttdata.common.events.CustomerDeletedEvent;
import com.nttdata.common.events.CustomerUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CustomerEventConsumer.class);
    private final CustomerService customerService;

    public CustomerEventConsumer(CustomerService customerService) {
        this.customerService = customerService;
    }

    @KafkaListener(topics = "customer.created", groupId = "account-service-group")
    public void handleCustomerCreated(CustomerCreatedEvent event) {
        logger.info("Received CustomerCreatedEvent: customerId={}, identification={}, name={}", 
                event.getCustomerId(), event.getIdentification(), event.getName());
        customerService.updateCustomerCache(event.getIdentification(), event.getName(), true);
    }

    @KafkaListener(topics = "customer.updated", groupId = "account-service-group")
    public void handleCustomerUpdated(CustomerUpdatedEvent event) {
        logger.info("Received CustomerUpdatedEvent: customerId={}, identification={}, name={}, state={}", 
                event.getCustomerId(), event.getIdentification(), event.getName(), event.getState());
        customerService.updateCustomerCache(event.getIdentification(), event.getName(), event.getState());
    }

    @KafkaListener(topics = "customer.deleted", groupId = "account-service-group")
    public void handleCustomerDeleted(CustomerDeletedEvent event) {
        logger.info("Received CustomerDeletedEvent: customerId={}, identification={}", 
                event.getCustomerId(), event.getIdentification());
        customerService.removeCustomerFromCache(event.getIdentification());
        // In production, you might want to mark associated accounts as inactive
    }
}



