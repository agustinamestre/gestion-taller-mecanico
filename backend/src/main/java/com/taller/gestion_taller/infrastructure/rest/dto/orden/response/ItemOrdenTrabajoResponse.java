package com.taller.gestion_taller.infrastructure.rest.dto.orden.response;

import java.math.BigDecimal;

public record ItemOrdenTrabajoResponse(
        Long id,
        Long productoId,
        String tipo,
        String descripcion,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) { }
