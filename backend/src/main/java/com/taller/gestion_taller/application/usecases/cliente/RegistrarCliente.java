package com.taller.gestion_taller.application.usecases.cliente;


import com.taller.gestion_taller.application.command.cliente.RegistrarClienteCommand;
import com.taller.gestion_taller.domain.model.Cliente;

public interface RegistrarCliente {
    Cliente registrarCliente(RegistrarClienteCommand command);
}
