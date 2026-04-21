package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ActualizarPrecioProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;

public interface ActualizarPrecioProducto {
    Producto actualizar(Long id, ActualizarPrecioProductoCommand command);
}
