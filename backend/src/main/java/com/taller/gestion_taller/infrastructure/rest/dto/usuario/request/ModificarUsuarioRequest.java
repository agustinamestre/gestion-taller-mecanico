package com.taller.gestion_taller.infrastructure.rest.dto.usuario.request;

import com.taller.gestion_taller.infrastructure.rest.validation.rol.RolValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModificarUsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,
        @NotNull(message = "El rol es obligatorio.")
        @RolValido
        String rol
) {}