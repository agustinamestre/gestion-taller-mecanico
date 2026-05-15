package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.ModificarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarOrdenTrabajoUseCase implements ModificarOrdenTrabajo  {

    private final OrdenTrabajoRepository ordenTrabajoRepository;

    @Override
    public OrdenTrabajo modificar(ModificarOrdenTrabajoCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.ordenId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.ordenId())));

        orden.modificar(command.descripcionProblema());
        return ordenTrabajoRepository.save(orden);
    }
}
