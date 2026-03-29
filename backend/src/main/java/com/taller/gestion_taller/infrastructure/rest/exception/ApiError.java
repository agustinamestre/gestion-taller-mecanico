package com.taller.gestion_taller.infrastructure.rest.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record ApiError(
        String errorCode,
        String errorMessage,
        @JsonIgnore String logMessage
) {
    public ApiError(String errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }
}
