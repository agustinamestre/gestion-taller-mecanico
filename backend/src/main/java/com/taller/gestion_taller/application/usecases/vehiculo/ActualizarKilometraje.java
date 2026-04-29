package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;

public interface ActualizarKilometraje {
    Vehiculo actualizar(Long id, ActualizarKilometrajeCommand command);
}
