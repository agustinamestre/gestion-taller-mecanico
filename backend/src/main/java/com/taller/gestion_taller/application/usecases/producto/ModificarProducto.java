package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ModificarProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;

public interface ModificarProducto {
    Producto modificar(Long id, ModificarProductoCommand command);
}
