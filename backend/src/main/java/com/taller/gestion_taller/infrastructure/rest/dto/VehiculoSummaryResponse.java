package com.taller.gestion_taller.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VehiculoSummaryResponse(
        Long id,
        String patente,
        String marca,
        String modelo,
        Integer anio,
        Integer kilometrajeActual,
        boolean activo
) {
}
