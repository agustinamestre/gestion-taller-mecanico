package com.taller.gestion_taller.infrastructure.rest.dto;

public record ClienteSummaryResponse(
        String dni,
        String nombre,
        String apellido
) {
}
