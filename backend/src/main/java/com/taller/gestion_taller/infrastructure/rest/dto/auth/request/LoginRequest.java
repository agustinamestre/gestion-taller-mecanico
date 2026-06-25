package com.taller.gestion_taller.infrastructure.rest.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El username no puede estar vacío.") String username,
        @NotBlank(message = "La contraseña no puede estar vacía.") String password
) {}