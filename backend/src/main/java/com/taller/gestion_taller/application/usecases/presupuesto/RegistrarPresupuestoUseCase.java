package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.application.mapper.PresupuestoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RegistrarPresupuestoUseCase implements RegistrarPresupuesto {

    private final PresupuestoRepository presupuestoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PresupuestoApplicationMapper presupuestoMapper;

    @Override
    public Presupuesto registrar(RegistrarPresupuestoCommand command) {
        Presupuesto nuevoPresupuesto = presupuestoMapper.commandToDomain(command);

        buscarVehiculoSiCorresponde(command.vehiculoId())
                .ifPresent(nuevoPresupuesto::setVehiculo);

        return presupuestoRepository.save(nuevoPresupuesto);
    }

    private Optional<Vehiculo> buscarVehiculoSiCorresponde(Long vehiculoId) {
        if (vehiculoId == null) {
            return Optional.empty();
        }
        return Optional.of(vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.vehiculoNoEncontrado(vehiculoId))));
    }
}
