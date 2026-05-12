package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.CambiarEstadoPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CambiarEstadoPresupuestoUseCase implements CambiarEstadoPresupuesto {

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public void cambiar(CambiarEstadoPresupuestoCommand command) {
        Presupuesto presupuesto = presupuestoRepository.findById(command.presupuestoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(command.presupuestoId())));

        presupuesto.cambiarEstado(command.nuevoEstado());
        presupuestoRepository.save(presupuesto);
    }
}