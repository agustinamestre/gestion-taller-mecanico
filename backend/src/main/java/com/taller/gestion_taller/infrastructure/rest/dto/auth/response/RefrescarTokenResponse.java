package com.taller.gestion_taller.infrastructure.rest.dto.auth.response;

public record RefrescarTokenResponse(
        String accessToken,
        String refreshToken
) {}