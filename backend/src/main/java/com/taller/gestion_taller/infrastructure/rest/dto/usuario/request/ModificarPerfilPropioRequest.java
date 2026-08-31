package com.taller.gestion_taller.infrastructure.rest.dto.usuario.request;

import com.taller.gestion_taller.infrastructure.rest.validation.email.Email;
import com.taller.gestion_taller.infrastructure.rest.validation.telefono.TelefonoValido;
import jakarta.validation.constraints.NotBlank;

public record ModificarPerfilPropioRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,
        @TelefonoValido
        String telefono,
        @Email
        String email,
        @NotBlank(message = "La dirección es obligatoria.")
        String direccion
) {}
