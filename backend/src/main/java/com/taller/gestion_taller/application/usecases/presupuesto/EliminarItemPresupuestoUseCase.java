package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.EliminarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EliminarItemPresupuestoUseCase implements EliminarItemPresupuesto{

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public void eliminar(EliminarItemPresupuestoCommand command) {
        Presupuesto presupuesto = presupuestoRepository.findById(command.presupuestoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(command.presupuestoId())));

        presupuesto.eliminarItem(command.itemId());
        presupuestoRepository.save(presupuesto);
    }
}
