package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.application.command.ModificarClienteCommand;
import com.taller.gestion_taller.domain.model.Cliente;

public interface ModificarCliente {
    Cliente modificarCliente(String nroDocumento, ModificarClienteCommand command);
}
