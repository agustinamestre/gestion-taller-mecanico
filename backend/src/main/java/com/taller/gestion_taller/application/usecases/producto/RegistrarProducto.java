package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;

public interface RegistrarProducto {
    Producto registrar(RegistrarProductoCommand command);
}
