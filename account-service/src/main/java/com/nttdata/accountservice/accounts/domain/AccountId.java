package com.nttdata.accountservice.accounts.domain;


import com.nttdata.core.domain.UUIDEntityId;

import java.util.UUID;

public class AccountId extends UUIDEntityId {
    public AccountId(UUID value) {
        super(value);
    }
}


