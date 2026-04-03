package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarClienteUseCase implements ListarCliente {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente listarCliente(String nroDocumento) {
        return clienteRepository.findByDni(nroDocumento)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(nroDocumento)));
    }
}
