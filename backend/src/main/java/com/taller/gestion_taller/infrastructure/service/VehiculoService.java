package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.application.usecases.vehiculo.RegistrarVehiculo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final RegistrarVehiculo registrarVehiculoUseCase;

    @Transactional
    public Vehiculo registrarVehiculo(RegistrarVehiculoCommand command) {
        return registrarVehiculoUseCase.registrar(command);
    }
}
