package com.taller.gestion_taller.application.command.orden;

import java.math.BigDecimal;

public record ModificarItemOrdenTrabajoCommand(
        Long ordenId,
        Long itemId,
        Long productoId,
        String descripcion,
        Integer cantidad,
        BigDecimal precioUnitario
) {}