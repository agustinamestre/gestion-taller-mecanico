package com.taller.gestion_taller.infrastructure.rest.dto.cliente.response;

public record ClienteSummaryResponse(
        String dni,
        String nombre,
        String apellido
) {
}
