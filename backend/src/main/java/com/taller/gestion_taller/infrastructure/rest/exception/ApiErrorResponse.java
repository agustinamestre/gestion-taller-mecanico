package com.taller.gestion_taller.infrastructure.rest.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        String uri,
        String timestamp,
        List<ApiError> error
) {
    public ApiErrorResponse(String uri, List<ApiError> error) {
        this(uri, LocalDateTime.now().toString(), error);
    }
}
