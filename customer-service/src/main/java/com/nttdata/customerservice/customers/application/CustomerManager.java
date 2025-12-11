package com.nttdata.customerservice.customers.application;

import com.nttdata.customerservice.customers.application.dto.CustomerRequestDTO;
import com.nttdata.customerservice.customers.application.dto.CustomerResponseDTO;
import com.nttdata.customerservice.customers.domain.Customer;
import com.nttdata.customerservice.customers.domain.CustomerRepository;
import com.nttdata.customerservice.config.KafkaEventPublisher;
import com.nttdata.common.events.CustomerCreatedEvent;
import com.nttdata.common.events.CustomerUpdatedEvent;
import com.nttdata.common.events.CustomerDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class CustomerManager {

    private static final Logger logger = LoggerFactory.getLogger(CustomerManager.class);
    private final CustomerRepository customerRepository;
    private final KafkaEventPublisher eventPublisher;

    public CustomerManager(CustomerRepository customerRepository, KafkaEventPublisher eventPublisher) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    public Mono<CustomerResponseDTO> storeCustomer(CustomerRequestDTO dto) {
        logger.info("Storing customer with identification: {}", dto.getIdentification());
        return Mono.fromCallable(() -> {
                    Customer customer = customerRepository.save(dto.toCustomer());
                    return customer;
                })
                .doOnNext(customer -> {
                    CustomerCreatedEvent event = new CustomerCreatedEvent(
                            customer.getId().getValue().toString(),
                            customer.getPerson().getId().getValue().toString(),
                            customer.getPerson().getIdentification(),
                            customer.getPerson().getName(),
                            LocalDateTime.now()
                    );
                    eventPublisher.publishCustomerCreated(event);
                    logger.info("Published CustomerCreatedEvent for customer: {}", customer.getId());
                })
                .map(CustomerResponseDTO::new);
    }

    public Mono<CustomerResponseDTO> updateCustomer(CustomerRequestDTO dto) {
        logger.info("Updating customer with identification: {}", dto.getIdentification());
        return Mono.fromCallable(() -> {
                    Customer customer = customerRepository.getByIdentification(dto.getIdentification());
                    customer.update(dto.toCustomer().getPerson(), dto.getPassword(), dto.getState());
                    return customerRepository.update(customer);
                })
                .doOnNext(customer -> {
                    CustomerUpdatedEvent event = new CustomerUpdatedEvent(
                            customer.getId().getValue().toString(),
                            customer.getPerson().getIdentification(),
                            customer.getPerson().getName(),
                            customer.getState(),
                            LocalDateTime.now()
                    );
                    eventPublisher.publishCustomerUpdated(event);
                    logger.info("Published CustomerUpdatedEvent for customer: {}", customer.getId());
                })
                .map(CustomerResponseDTO::new);
    }

    public Mono<CustomerResponseDTO> getCustomer(String identification) {
        logger.info("Getting customer with identification: {}", identification);
        return Mono.fromCallable(() -> {
                    Customer customer = customerRepository.getByIdentification(identification);
                    return customer;
                })
                .map(CustomerResponseDTO::new);
    }

    public Mono<Void> deleteCustomer(String identification) {
        logger.info("Deleting customer with identification: {}", identification);
        return Mono.fromCallable(() -> {
                    Customer customer = customerRepository.getByIdentification(identification);
                    String customerId = customer.getId().getValue().toString();
                    customerRepository.delete(identification);
                    return customerId;
                })
                .doOnNext(customerId -> {
                    CustomerDeletedEvent event = new CustomerDeletedEvent(
                            customerId,
                            identification,
                            LocalDateTime.now()
                    );
                    eventPublisher.publishCustomerDeleted(event);
                    logger.info("Published CustomerDeletedEvent for customer: {}", customerId);
                })
                .then();
    }
}


