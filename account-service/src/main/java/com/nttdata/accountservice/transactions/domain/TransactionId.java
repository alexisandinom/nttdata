package com.nttdata.accountservice.transactions.domain;


import com.nttdata.core.domain.UUIDEntityId;

import java.util.UUID;

public class TransactionId extends UUIDEntityId {
    public TransactionId(UUID value) {
        super(value);
    }
}


