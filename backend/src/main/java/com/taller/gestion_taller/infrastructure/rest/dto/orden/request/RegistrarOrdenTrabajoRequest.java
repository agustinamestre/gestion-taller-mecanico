package com.taller.gestion_taller.infrastructure.rest.dto.orden.request;

import jakarta.validation.constraints.NotNull;

public record RegistrarOrdenTrabajoRequest(
        String patente,
        Long presupuestoId,
        String descripcionProblema,
        @NotNull(message = "El usuario de creacion es obligatorio")
        Long usuarioCreacionId
) {}
