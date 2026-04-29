package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.ModificarVehiculoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarVehiculoUseCase implements ModificarVehiculo {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public Vehiculo modificar(Long id, ModificarVehiculoCommand command) {
        Vehiculo vehiculo = buscarVehiculo(id);
        Cliente cliente = buscarClienteActivo(command.clienteId());
        Modelo modelo = buscarModeloActivo(command.modeloId());

        return vehiculoRepository.save(
                vehiculo.actualizarDatos(modelo, command.anio(), cliente)
        );
    }

    private Vehiculo buscarVehiculo(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado(id)));
    }

    private Cliente buscarClienteActivo(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.clienteNoEncontrado(clienteId)));
        cliente.requerirActivo();
        return cliente;
    }

    private Modelo buscarModeloActivo(Long modeloId) {
        Modelo modelo = modeloRepository.findById(modeloId)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.modeloNoEncontrado()));
        modelo.requerirActivo();
        return modelo;
    }
}
