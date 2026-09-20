package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request;

import com.taller.gestion_taller.infrastructure.rest.validation.dni.DniValido;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PresupuestoRequest(
        @Positive(message = "debe ser un numero positivo")
        Long vehiculoId,

        @DniValido
        String dni,

        @Size(max = 500, message = "no puede superar los 500 caracteres")
        String observaciones
) {}
