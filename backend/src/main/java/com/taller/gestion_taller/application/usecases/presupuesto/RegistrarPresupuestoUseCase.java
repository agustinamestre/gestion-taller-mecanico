package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarPresupuestoUseCase implements RegistrarPresupuesto {

    private final PresupuestoRepository presupuestoRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public Presupuesto registrar(RegistrarPresupuestoCommand command) {
        Vehiculo vehiculo = buscarVehiculoSiCorresponde(command.vehiculoId());

        Presupuesto nuevoPresupuesto = Presupuesto.crearNuevo(
                vehiculo,
                command.observaciones()
        );

        return presupuestoRepository.save(nuevoPresupuesto);
    }

    private Vehiculo buscarVehiculoSiCorresponde(Long vehiculoId) {
        if (vehiculoId == null) {
            return null;
        }
        return vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado(vehiculoId)));
    }
}
