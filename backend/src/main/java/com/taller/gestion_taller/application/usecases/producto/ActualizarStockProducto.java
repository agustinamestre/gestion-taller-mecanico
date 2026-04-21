package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ActualizarStockProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;

public interface ActualizarStockProducto {
    Producto actualizar(Long id, ActualizarStockProductoCommand command);
}
