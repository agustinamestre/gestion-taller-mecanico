package com.taller.gestion_taller.infrastructure.rest.dto;

import java.time.LocalDate;

public record ClienteResponse(
        Long id,
        String dni,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion,
        boolean activo,
        LocalDate fechaCreacion,
        LocalDate fechaModificacion

) {
}
