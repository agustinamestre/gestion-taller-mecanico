package com.taller.gestion_taller.infrastructure.rest.dto.producto.response;

import com.taller.gestion_taller.domain.model.TipoProducto;

import java.math.BigDecimal;

public record ProductoResponse (
         Long id,
         String nombre,
         String descripcion,
         TipoProducto tipo,
         BigDecimal precioActual,
         Integer stockActual,
         boolean activo
) { }
