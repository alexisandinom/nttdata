package com.nttdata.customerservice.persons.domain;


import com.nttdata.core.domain.UUIDEntityId;

import java.util.UUID;

public class PersonId extends UUIDEntityId {
    public PersonId(UUID value) {
        super(value);
    }
}


