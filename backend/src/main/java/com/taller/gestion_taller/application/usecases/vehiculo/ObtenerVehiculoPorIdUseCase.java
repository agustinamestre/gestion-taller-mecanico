package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObtenerVehiculoPorIdUseCase implements ObtenerVehiculoPorId {

    private final VehiculoRepository vehiculoRepository;

    @Override
    public Vehiculo obtener(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado(id)));
    }
}
