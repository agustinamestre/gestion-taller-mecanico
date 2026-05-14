package com.taller.gestion_taller.infrastructure.rest.dto.orden.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdenTrabajoRequest(
        @NotBlank(message = "La patente es obligatoria")
        String patente,
        Long presupuestoId,
        @NotBlank(message = "La descripcion del problema es obligatoria")
        String descripcionProblema,
        @NotNull(message = "El usuario de creacion es obligatorio")
        Long usuarioCreacionId
) {}
