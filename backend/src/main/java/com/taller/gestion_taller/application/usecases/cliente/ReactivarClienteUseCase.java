package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReactivarClienteUseCase implements ReactivarCliente {

    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public void reactivar(String nroDocumento) {
        Cliente cliente = clienteRepository.findByDni(nroDocumento)
                .map(Cliente::reactivar)
                .map(clienteRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(nroDocumento)));

        vehiculoRepository.findByClienteId(cliente.getId()).stream()
                .filter(vehiculo -> !vehiculo.isActivo())
                .map(Vehiculo::reactivar)
                .forEach(vehiculoRepository::save);
    }
}
