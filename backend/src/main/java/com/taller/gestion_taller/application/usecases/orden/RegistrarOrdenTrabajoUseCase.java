package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.application.mapper.OrdenTrabajoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarOrdenTrabajoUseCase implements RegistrarOrdenTrabajo {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final OrdenTrabajoApplicationMapper ordenTrabajoMapper;

    @Override
    public OrdenTrabajo registrar(RegistrarOrdenTrabajoCommand command) {
        Vehiculo vehiculo = vehiculoRepository.findByPatente(command.patente())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.vehiculoNoEncontrado(command.patente())));

        OrdenTrabajo orden = ordenTrabajoMapper.commandToDomain(command);
        orden.setVehiculo(vehiculo);

        if (command.presupuestoId() != null) {
            Presupuesto presupuesto = presupuestoRepository.findById(command.presupuestoId())
                    .orElseThrow(() -> new NotFoundException(
                            BusinessErrors.presupuestoNoEncontrado(command.presupuestoId())));

            if (presupuesto.getEstado() != EstadoPresupuesto.APROBADO) {
                throw new BusinessRunTimeException(
                        BusinessErrors.presupuestoDebeEstarAprobado());
            }

            orden.setPresupuesto(presupuesto);
        }

        return ordenTrabajoRepository.save(orden);
    }
}
