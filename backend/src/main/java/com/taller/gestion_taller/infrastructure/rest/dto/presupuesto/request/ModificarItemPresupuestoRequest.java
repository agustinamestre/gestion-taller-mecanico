package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ModificarItemPresupuestoRequest(
        Long productoId,

        @NotBlank(message = "La descripcion es requerida")
        String descripcion,

        @NotNull(message = "La cantidad es requerida")
        Integer cantidad,

        @NotNull(message = "El precio unitario es requerido")
        BigDecimal precioUnitario
) { }
