package com.taller.gestion_taller.infrastructure.rest.dto.usuario.request;

import jakarta.validation.constraints.NotBlank;

public record ModificarPerfilPropioRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido
) {}
