package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.vehiculo.RegistrarVehiculoCommand;
import com.taller.gestion_taller.application.mapper.VehiculoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.VehiculoValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarVehiculoUseCase implements RegistrarVehiculo {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoValidator vehiculoValidator;
    private final VehiculoApplicationMapper vehiculoApplicationMapper;

    @Override
    public Vehiculo registrar(RegistrarVehiculoCommand command) {
        vehiculoValidator.validarPatenteUnica(command.patente());

        Cliente cliente = buscarClienteActivo(command.clienteId());
        Modelo modelo = buscarModeloActivo(command.modeloId());

        Vehiculo nuevoVehiculo = vehiculoApplicationMapper.commandToDomain(command, modelo, cliente);

        return vehiculoRepository.save(nuevoVehiculo);
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
