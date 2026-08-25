package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarVehiculosUseCase implements ListarVehiculos {
    private final VehiculoRepository vehiculoRepository;

    @Override
    public List<Vehiculo> listar(String patente) {
        if (patente == null || patente.isBlank()) {
            return vehiculoRepository.findByActivoTrue();
        }
        return vehiculoRepository.findByPatenteContainingAndActivoTrue(patente);
    }
}
