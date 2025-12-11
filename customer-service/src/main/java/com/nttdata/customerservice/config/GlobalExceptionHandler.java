package com.nttdata.customerservice.config;

import com.nttdata.core.application.ErrorResponse;
import com.nttdata.core.exceptions.EntityNotFoundException;
import com.nttdata.core.exceptions.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleEntityNotFound(EntityNotFoundException ex) {
        logger.warn("Entity not found: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), null)));
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidArgument(InvalidArgumentException ex) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getName(), ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), errors)));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        logger.warn("Validation failed: {}", errors);
        return Mono.just(ResponseEntity.badRequest()
                .body(new ErrorResponse("Validation failed", errors)));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralExceptions(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage(), null)));
    }
}



