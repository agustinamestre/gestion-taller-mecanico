package com.taller.gestion_taller.infrastructure.rest.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record RefrescarTokenRequest(
        @NotBlank(message = "es obligatorio")
        String refreshToken
) {}