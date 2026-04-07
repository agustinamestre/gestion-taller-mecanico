package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.application.command.ModificarClienteCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarClienteUseCase implements ModificarCliente {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente modificarCliente(String nroDocumento, ModificarClienteCommand command) {
        return clienteRepository.findByDni(nroDocumento)
                .map(cliente -> cliente.actualizarDatos(command))
                .map(clienteRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(nroDocumento)));
    }
}
