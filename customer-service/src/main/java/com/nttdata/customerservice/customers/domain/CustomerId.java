package com.nttdata.customerservice.customers.domain;


import com.nttdata.core.domain.UUIDEntityId;

import java.util.UUID;

public class CustomerId extends UUIDEntityId {
    public CustomerId(UUID value) {
        super(value);
    }
}


