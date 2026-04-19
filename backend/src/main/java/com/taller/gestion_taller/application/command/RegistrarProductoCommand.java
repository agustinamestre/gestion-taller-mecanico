package com.taller.gestion_taller.application.command;

import java.math.BigDecimal;

public record RegistrarProductoCommand(
        String nombre,
        String descripcion,
        String tipo,
        BigDecimal precioActual,
        Integer stockActual)
{ }
