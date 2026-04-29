package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualizarKilometrajeRequest(
        @NotNull(message = "El kilometraje es obligatorio")
        @Min(value = 0, message = "El kilometraje debe ser mayor o igual a 0")
        Integer kilometrajeActual
) {}
