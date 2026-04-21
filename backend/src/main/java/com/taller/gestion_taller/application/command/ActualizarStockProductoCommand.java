package com.taller.gestion_taller.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActualizarStockProductoCommand {
    private Integer nuevoStock;
}
