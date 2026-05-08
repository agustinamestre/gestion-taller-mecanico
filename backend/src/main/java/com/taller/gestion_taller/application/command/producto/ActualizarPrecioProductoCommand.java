package com.taller.gestion_taller.application.command.producto;

import java.math.BigDecimal;

public record ActualizarPrecioProductoCommand (
        BigDecimal nuevoPrecio
) { }
