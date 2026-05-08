package com.taller.gestion_taller.application.command.presupuesto;

import java.math.BigDecimal;

public record AgregarItemPresupuestoCommand(
        Long presupuestoId,
        Long productoId,
        String descripcion,
        Integer cantidad,
        BigDecimal precioUnitario
) { }
