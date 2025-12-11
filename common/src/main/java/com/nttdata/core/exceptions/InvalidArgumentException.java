package com.nttdata.core.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidArgumentException extends DomainException {
    private final String name;

    public InvalidArgumentException(String name, String message) {
        super(message);
        this.name = name;
    }

    public InvalidArgumentException(String name) {
        this.name = name;
    }
}

