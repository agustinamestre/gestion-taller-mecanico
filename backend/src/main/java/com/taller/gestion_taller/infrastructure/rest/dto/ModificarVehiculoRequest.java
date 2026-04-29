package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.NotNull;

public record ModificarVehiculoRequest(
        @NotNull(message = "El modelo es obligatorio")
        Long modeloId,

        @NotNull(message = "El año es obligatorio")
        Integer anio,

        @NotNull(message = "El cliente es obligatorio")
        Long clienteId
) {}
