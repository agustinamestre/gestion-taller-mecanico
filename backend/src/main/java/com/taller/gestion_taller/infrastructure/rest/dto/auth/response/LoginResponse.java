package com.taller.gestion_taller.infrastructure.rest.dto.auth.response;

public record LoginResponse(
        String token,
        String username,
        String rol
) {}