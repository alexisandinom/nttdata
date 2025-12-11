package com.nttdata.accountservice.accounts.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Reference to a customer from the customer-service.
 * This is used for eventual consistency in microservices architecture.
 */
@Getter
@AllArgsConstructor
public class CustomerReference {
    private String customerId;
    private String identification;
    private String name;
    private Boolean active;
}



