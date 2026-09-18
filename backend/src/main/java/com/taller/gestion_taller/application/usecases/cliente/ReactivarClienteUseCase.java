package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReactivarClienteUseCase implements ReactivarCliente {

    private final ClienteRepository clienteRepository;

    @Override
    public void reactivar(String nroDocumento) {
        clienteRepository.findByDni(nroDocumento)
                .map(Cliente::reactivar)
                .map(clienteRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(nroDocumento)));
    }
}
