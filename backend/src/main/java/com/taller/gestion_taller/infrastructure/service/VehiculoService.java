package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.vehiculo.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.application.command.vehiculo.ModificarVehiculoCommand;
import com.taller.gestion_taller.application.command.vehiculo.RegistrarVehiculoCommand;
import com.taller.gestion_taller.application.usecases.vehiculo.*;
import com.taller.gestion_taller.domain.model.Vehiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final RegistrarVehiculo registrarVehiculoUseCase;
    private final ListarVehiculos listarVehiculosUseCase;
    private final GetVehiculoByPatente getVehiculoByPatenteUseCase;
    private final ModificarVehiculo modificarVehiculoUseCase;
    private final ActualizarKilometraje actualizarKilometrajeUseCase;
    private final DesactivarVehiculo desactivarVehiculoUseCase;

    @Transactional
    public Vehiculo registrarVehiculo(RegistrarVehiculoCommand command) {
        return registrarVehiculoUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> listar(String patente) {
        return listarVehiculosUseCase.listar(patente);
    }

    @Transactional(readOnly = true)
    public Vehiculo getVehiculoByPatente(String patente) {
        return getVehiculoByPatenteUseCase.getByPatente(patente);
    }

    @Transactional
    public Vehiculo modificarVehiculo(Long id, ModificarVehiculoCommand command) {
        return modificarVehiculoUseCase.modificar(id, command);
    }
    
    @Transactional
    public Vehiculo actualizarKilometraje(Long id, ActualizarKilometrajeCommand command) {
        return actualizarKilometrajeUseCase.actualizar(id, command);
    }

    @Transactional
    public void desactivarVehiculo(Long id) {
        desactivarVehiculoUseCase.desactivarVehiculo(id);
    }
}
