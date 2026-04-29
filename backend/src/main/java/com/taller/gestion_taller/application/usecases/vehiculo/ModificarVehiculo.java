package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.ModificarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;

public interface ModificarVehiculo {
    Vehiculo modificar(Long id, ModificarVehiculoCommand command);
}
