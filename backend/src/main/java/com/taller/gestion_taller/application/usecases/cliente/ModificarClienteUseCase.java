package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.application.command.cliente.ModificarClienteCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarClienteUseCase implements ModificarCliente {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente modificarCliente(String dniActual, ModificarClienteCommand command) {
        Cliente cliente = clienteRepository.findByDni(dniActual)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(dniActual)));

        boolean cambioDni = !cliente.getDni().equals(command.dniNuevo());
        if (cambioDni) {
            clienteRepository.findByDni(command.dniNuevo())
                    .ifPresent(c -> {
                        throw new BusinessRunTimeException(BusinessErrors.dniYaRegistrado());
                    });
        }

        Cliente clienteActualizado = cliente.actualizarDatos(command);
        return clienteRepository.save(clienteActualizado);
    }
}
