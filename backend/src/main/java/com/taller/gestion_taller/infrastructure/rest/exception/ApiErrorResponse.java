package com.taller.gestion_taller.infrastructure.rest.exception;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
public record ApiErrorResponse(
        String uri,
        LocalDateTime timestamp,
        List<ApiError> error
) {
    public ApiErrorResponse(String uri, List<ApiError> error) {
        this(uri, LocalDateTime.now(), error);
    }

    public static ApiErrorResponse crearResponseConError(String uri, ApiError error) {
        logError(error);
        return new ApiErrorResponse(uri, LocalDateTime.now(), List.of(error));
    }

    private static void logError(ApiError error) {
        if (nonNull(error.errorMessage())) {
            log.error("Ocurrio un error al procesar la solicitud: {}", error.errorMessage());
        }
    }
}
