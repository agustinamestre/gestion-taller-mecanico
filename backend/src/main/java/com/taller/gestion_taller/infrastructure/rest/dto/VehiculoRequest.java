package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehiculoRequest(
        @NotBlank(message = "La patente es obligatoria")
        String patente,

        @NotNull(message = "El modelo es obligatorio")
        Long modeloId,

        @NotNull(message = "El año es obligatorio")
        Integer anio,

        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,

        @NotNull(message = "El kilometraje es obligatorio")
        @Min(value = 0, message = "El kilometraje debe ser mayor o igual a 0")
        Integer kilometrajeActual
) {}
