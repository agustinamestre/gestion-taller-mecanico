package com.taller.gestion_taller.domain.exception;

public record BusinessError(
        String code,
        String message,
        String logDetail
) {
    public BusinessError(String code, String message) {
        this(code, message, null);
    }
}