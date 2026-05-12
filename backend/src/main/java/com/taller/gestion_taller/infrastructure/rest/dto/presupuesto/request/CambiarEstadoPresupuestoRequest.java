package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoPresupuestoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoPresupuesto nuevoEstado
) { }
