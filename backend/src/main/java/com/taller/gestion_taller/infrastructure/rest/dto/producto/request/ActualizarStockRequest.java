package com.taller.gestion_taller.infrastructure.rest.dto.producto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualizarStockRequest (
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser mayor o igual a cero")
        Integer nuevoStock
) { }
