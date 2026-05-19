package com.taller.gestion_taller.infrastructure.rest.dto.orden.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ModificarItemOrdenTrabajoRequest(
        Long productoId,
        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion,
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        Integer cantidad,
        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precioUnitario
) { }
