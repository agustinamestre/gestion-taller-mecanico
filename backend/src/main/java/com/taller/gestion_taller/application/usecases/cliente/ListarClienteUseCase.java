package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarClienteUseCase implements ListarCliente {

    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public Cliente listarCliente(String nroDocumento) {
        Cliente cliente = clienteRepository.findByDni(nroDocumento)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(nroDocumento)));
    
        List<Vehiculo> vehiculos = vehiculoRepository.findByClienteId(cliente.getId());
    
        return cliente.toBuilder()
                .vehiculos(vehiculos)
                .build();
    }
}
