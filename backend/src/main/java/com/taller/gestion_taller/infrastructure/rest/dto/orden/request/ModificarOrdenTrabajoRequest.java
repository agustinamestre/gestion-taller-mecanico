package com.taller.gestion_taller.infrastructure.rest.dto.orden.request;

import jakarta.validation.constraints.NotBlank;

public record ModificarOrdenTrabajoRequest(
        @NotBlank(message = "La descripcion del problema es obligatoria")
        String descripcionProblema
) {}