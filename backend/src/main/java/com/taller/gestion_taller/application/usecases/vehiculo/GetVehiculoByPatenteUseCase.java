package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetVehiculoByPatenteUseCase implements GetVehiculoByPatente {

    private final VehiculoRepository vehiculoRepository;

    @Override
    public Vehiculo getByPatente(String patente) {
        return vehiculoRepository.findByPatente(patente)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado(patente)));
    }
}
