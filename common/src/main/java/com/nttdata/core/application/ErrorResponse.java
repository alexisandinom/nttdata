package com.nttdata.core.application;

import java.util.Map;

public record ErrorResponse(
        String message,
        Map<String, String> errors) {
}
