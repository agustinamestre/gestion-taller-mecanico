package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CambiarEstadoOrdenTrabajoUseCase implements CambiarEstadoOrdenTrabajo {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final PresupuestoRepository presupuestoRepository;

    @Override
    public void cambiar(CambiarEstadoOrdenTrabajoCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.ordenId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.ordenId())));

        orden.cambiarEstado(command.nuevoEstado());
        ordenTrabajoRepository.save(orden);

        if (command.nuevoEstado() == EstadoOrdenTrabajo.CANCELADO && orden.getPresupuesto() != null) {
            cancelarPresupuesto(orden.getPresupuesto().getId());
        }
    }

    private void cancelarPresupuesto(Long presupuestoId) {
        Presupuesto presupuesto = presupuestoRepository.findById(presupuestoId)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(presupuestoId)));

        presupuesto.cancelar();
        presupuestoRepository.save(presupuesto);
    }
}