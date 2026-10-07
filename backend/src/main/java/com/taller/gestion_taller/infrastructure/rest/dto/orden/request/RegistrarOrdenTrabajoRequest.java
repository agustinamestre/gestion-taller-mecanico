package com.taller.gestion_taller.infrastructure.rest.dto.orden.request;

import jakarta.validation.constraints.Min;

public record RegistrarOrdenTrabajoRequest(
        String patente,
        Long presupuestoId,
        String descripcionProblema,
        boolean incluyeService,
        @Min(value = 0, message = "El kilometraje de ingreso debe ser mayor o igual a 0")
        Integer kilometrajeIngreso
) {}
