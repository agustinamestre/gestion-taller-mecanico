package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.application.mapper.VehiculoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
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

        Cliente cliente = obtenerClienteActivo(command.clienteId());
        Modelo modelo = obtenerModeloActivo(command.modeloId());

        Vehiculo nuevoVehiculo = vehiculoApplicationMapper.commandToDomain(command, modelo, cliente);

        return vehiculoRepository.save(nuevoVehiculo);
    }

    private Cliente obtenerClienteActivo(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .map(cliente -> {
                    if (!cliente.isActivo()) {
                        throw new BusinessRunTimeException(BusinessErrors.operacionConClienteInactivo());
                    }
                    return cliente;
                })
                .orElseThrow(() -> new BusinessRunTimeException(BusinessErrors.clienteNoEncontrado(clienteId)));
    }

    private Modelo obtenerModeloActivo(Long modeloId) {
        return modeloRepository.findById(modeloId)
                .map(modelo -> {
                    if (!modelo.isActivo()) {
                        throw new BusinessRunTimeException(BusinessErrors.modeloYaDesactivado());
                    }
                    return modelo;
                })
                .orElseThrow(() -> new BusinessRunTimeException(BusinessErrors.modeloNoEncontrado()));
    }
}
