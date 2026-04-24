package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;

public interface RegistrarVehiculo {
    Vehiculo registrar(RegistrarVehiculoCommand command);
}
