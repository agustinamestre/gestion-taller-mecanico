package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PresupuestoRequest(
        @Positive(message = "debe ser un numero positivo")
        Long vehiculoId,

        @Size(max = 500, message = "no puede superar los 500 caracteres")
        String observaciones
) {}
