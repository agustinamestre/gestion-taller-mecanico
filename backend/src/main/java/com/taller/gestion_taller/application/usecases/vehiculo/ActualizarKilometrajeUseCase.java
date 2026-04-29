package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActualizarKilometrajeUseCase implements ActualizarKilometraje {

    private final VehiculoRepository vehiculoRepository;

    @Override
    public Vehiculo actualizar(Long id, ActualizarKilometrajeCommand command) {
        Vehiculo vehiculoExistente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado(id)));

        Vehiculo vehiculoActualizado = vehiculoExistente.actualizarKilometraje(command.kilometrajeActual());
        return vehiculoRepository.save(vehiculoActualizado);
    }
}
