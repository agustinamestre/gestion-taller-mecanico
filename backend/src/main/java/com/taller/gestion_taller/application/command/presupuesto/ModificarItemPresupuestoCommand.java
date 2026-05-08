package com.taller.gestion_taller.application.command.presupuesto;

import java.math.BigDecimal;

public record ModificarItemPresupuestoCommand(
        Long presupuestoId,
        Long itemId,
        Long productoId,       // null = no cambia el producto
        String descripcion,
        Integer cantidad,
        BigDecimal precioUnitario
) {}
