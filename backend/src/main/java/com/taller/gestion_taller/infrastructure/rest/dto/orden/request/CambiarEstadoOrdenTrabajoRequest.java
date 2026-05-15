package com.taller.gestion_taller.infrastructure.rest.dto.orden.request;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoOrdenTrabajoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoOrdenTrabajo nuevoEstado
) {}