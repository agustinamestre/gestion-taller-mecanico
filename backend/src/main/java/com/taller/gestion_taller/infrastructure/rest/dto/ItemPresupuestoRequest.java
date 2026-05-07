package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ItemPresupuestoRequest(
        @NotBlank(message = "El tipo de producto es requerido")
        String tipo,

        @NotNull(message = "El producto es requerido")
        Long productoId,

        @NotBlank(message = "La descripcion es requerida")
        String descripcion,

        @NotNull(message = "La cantidad es requerida")
        Integer cantidad,

        @NotNull(message = "El precio unitario es requerido")
        @PositiveOrZero(message = "debe ser mayor o igual a cero")
        BigDecimal precioUnitario
) {
}
