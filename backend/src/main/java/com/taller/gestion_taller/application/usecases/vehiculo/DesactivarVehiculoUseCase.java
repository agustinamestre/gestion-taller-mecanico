package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesactivarVehiculoUseCase implements DesactivarVehiculo {

    private final VehiculoRepository vehiculoRepository;

    @Override
    public void desactivarVehiculo(Long id) {
        vehiculoRepository.findById(id)
                .map(Vehiculo::desactivar)
                .map(vehiculoRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado()));
    }
}
