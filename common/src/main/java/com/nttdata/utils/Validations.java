package com.nttdata.utils;


import com.nttdata.core.exceptions.InvalidArgumentException;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * Provides methods to validate domain attributes
 */
public class Validations {

    /**
     * Validate that an object not be null
     *
     * @param obj  the object to validate
     * @param name the name of attribute
     * @param <T>  type of attribute
     * @return the object in case it will be different from null
     * @throws InvalidArgumentException if the object is null
     */
    public static <T> T validateNonNull(T obj, String name) throws InvalidArgumentException {
        if (isNull(obj)) {
            throw new InvalidArgumentException(name, "cannot be null");
        }
        return obj;
    }

    public static Map<String, String> createErrorResponse(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return errorResponse;
    }
}
