package com.nttdata.core.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EntityNotFoundException extends DomainException {
    private final Class clazz;
    private final String id;
}
