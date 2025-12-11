package com.nttdata.core.domain;

import com.nttdata.core.exceptions.DomainException;

import java.util.UUID;

public class UUIDEntityId extends EntityId<UUID> {

    protected UUIDEntityId(UUID value) {
        super(value);
    }

    public static <T extends UUIDEntityId> T create(Class<T> clazz) throws DomainException {
        try {
            return clazz.getDeclaredConstructor(new Class[]{UUID.class}).newInstance(UUID.randomUUID());
        } catch (Exception e) {
            throw new DomainException(e);
        }
    }
}
