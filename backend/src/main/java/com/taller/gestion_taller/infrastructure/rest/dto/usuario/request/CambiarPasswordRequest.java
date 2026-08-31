package com.taller.gestion_taller.infrastructure.rest.dto.usuario.request;

import jakarta.validation.constraints.NotBlank;

public record CambiarPasswordRequest(
        @NotBlank(message = "La contraseña actual es obligatoria.")
        String passwordActual,
        @NotBlank(message = "La nueva contraseña es obligatoria.")
        String passwordNueva
) {}
