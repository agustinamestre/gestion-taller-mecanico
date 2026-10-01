package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReactivarVehiculoUseCase implements ReactivarVehiculo {

    private final VehiculoRepository vehiculoRepository;

    @Override
    public void reactivarVehiculo(Long id) {
        vehiculoRepository.findById(id)
                .map(Vehiculo::reactivar)
                .map(vehiculoRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado()));
    }
}
