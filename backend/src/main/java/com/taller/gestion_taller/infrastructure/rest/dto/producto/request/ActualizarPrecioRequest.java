package com.taller.gestion_taller.infrastructure.rest.dto.producto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ActualizarPrecioRequest (
        @NotNull(message = "El precio es obligatorio")
        BigDecimal nuevoPrecio
) { }
