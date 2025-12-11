package com.nttdata.core.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EntityNotFoundException extends RuntimeException {
    private final Class<?> clazz;
    private final String id;

    // Constructor for cases where we only have a message
    public EntityNotFoundException(String message) {
        super(message);
        this.clazz = null;
        this.id = null;
    }

    // Constructor with Class and id
    public EntityNotFoundException(Class<?> clazz, String id) {
        super("Entity of type " + clazz.getSimpleName() + " with id " + id + " not found");
        this.clazz = clazz;
        this.id = id;
    }

    // Constructor with Class, id, and custom message
    public EntityNotFoundException(Class<?> clazz, String id, String message) {
        super(message);
        this.clazz = clazz;
        this.id = id;
    }
}
