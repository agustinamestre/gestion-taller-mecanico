package com.taller.gestion_taller.infrastructure.rest.dto.usuario.request;

import com.taller.gestion_taller.infrastructure.rest.validation.rol.RolValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
        @NotBlank(message = "El username es obligatorio.")
        String username,
        @NotBlank(message = "La contraseña es obligatoria.")
        String password,
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,
        @NotNull(message = "El rol es obligatorio.")
        @RolValido
        String rol
) {}