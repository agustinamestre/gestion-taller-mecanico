package com.taller.gestion_taller.infrastructure.rest.dto;

import com.taller.gestion_taller.infrastructure.rest.validation.dni.DniValido;
import com.taller.gestion_taller.infrastructure.rest.validation.email.Email;
import com.taller.gestion_taller.infrastructure.rest.validation.telefono.TelefonoValido;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @DniValido
        String dni,
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,
        @TelefonoValido
        String telefono,
        @Email
        String email,
        String direccion
) { }