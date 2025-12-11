package com.nttdata.core.exceptions;

import lombok.Getter;

@Getter
public class DomainException extends Exception {
    private String[] params;

    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }

    public DomainException(String message, String[] params) {
        super(message);
        this.params = params;
    }
}
