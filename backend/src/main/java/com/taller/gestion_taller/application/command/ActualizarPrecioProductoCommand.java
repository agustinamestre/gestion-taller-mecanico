package com.taller.gestion_taller.application.command;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ActualizarPrecioProductoCommand {
    private BigDecimal nuevoPrecio;
}
