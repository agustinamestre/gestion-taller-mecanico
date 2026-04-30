package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.ModificarClienteCommand;
import com.taller.gestion_taller.application.command.RegistrarClienteCommand;
import com.taller.gestion_taller.application.usecases.cliente.DarDeBajaCliente;
import com.taller.gestion_taller.application.usecases.cliente.ListarCliente;
import com.taller.gestion_taller.application.usecases.cliente.ListarClientes;
import com.taller.gestion_taller.application.usecases.cliente.ModificarCliente;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarCliente;
import com.taller.gestion_taller.domain.model.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final RegistrarCliente registrarClienteUseCase;
    private final ListarClientes listarClientesUseCase;
    private final ListarCliente listarClienteUseCase;
    private final ModificarCliente modificarClienteUseCase;
    private final DarDeBajaCliente darDeBajaClienteUseCase;

    @Transactional
    public Cliente registrarCliente(RegistrarClienteCommand command) {
        return registrarClienteUseCase.registrarCliente(command);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return listarClientesUseCase.listarClientes();
    }

    @Transactional(readOnly = true)
    public Cliente listarCliente(String nroDocumento) {
        return listarClienteUseCase.listarCliente(nroDocumento);
    }

    @Transactional
    public Cliente modificarCliente(String nroDocumento, ModificarClienteCommand command) {
        return modificarClienteUseCase.modificarCliente(nroDocumento, command);
    }

    @Transactional
    public void darDeBajaCliente(String nroDocumento) {
        darDeBajaClienteUseCase.darDeBaja(nroDocumento);
    }
}
