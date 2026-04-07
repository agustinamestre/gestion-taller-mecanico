package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DarDeBajaClienteUseCase implements DarDeBajaCliente{

    private final ClienteRepository clienteRepository;

    @Override
    public void darDeBaja(String nroDocumento) {
        clienteRepository.findByDni(nroDocumento)
                .map(Cliente::darDeBaja)
                .map(clienteRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(nroDocumento)));

    }
}
