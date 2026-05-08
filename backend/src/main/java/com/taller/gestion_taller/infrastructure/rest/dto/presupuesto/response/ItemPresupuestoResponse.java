package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response;

import java.math.BigDecimal;

public record ItemPresupuestoResponse (
        Long id,
        Long presupuestoId,
        Long productoId,
        String tipo,
        String descripcion,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) { }
