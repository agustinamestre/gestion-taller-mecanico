package com.taller.gestion_taller.application.command.orden;

import java.math.BigDecimal;

public record AgregarItemOrdenTrabajoCommand(
        Long ordenId,
        Long productoId,
        String descripcion,
        Integer cantidad,
        BigDecimal precioUnitario
) {}