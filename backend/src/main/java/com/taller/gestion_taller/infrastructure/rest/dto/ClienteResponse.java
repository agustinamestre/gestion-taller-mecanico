package com.taller.gestion_taller.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
        LocalDate fechaModificacion,
        List<VehiculoSummaryResponse> vehiculos

) {
}
