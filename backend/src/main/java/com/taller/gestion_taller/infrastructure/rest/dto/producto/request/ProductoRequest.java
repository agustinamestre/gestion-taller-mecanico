package com.taller.gestion_taller.infrastructure.rest.dto.producto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductoRequest (
        @NotBlank(message = "El nombre del producto no puede estar vacío")
        String nombre,
        String descripcion,
        @NotBlank(message = "El tipo de producto es obligatorio")
        String tipo,
        BigDecimal precioActual,
        Integer stockActual
){ }
