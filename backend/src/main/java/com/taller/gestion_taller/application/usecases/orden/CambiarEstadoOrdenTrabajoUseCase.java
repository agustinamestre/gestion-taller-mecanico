package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CambiarEstadoOrdenTrabajoUseCase implements CambiarEstadoOrdenTrabajo {

    private final OrdenTrabajoRepository ordenTrabajoRepository;

    @Override
    public void cambiar(CambiarEstadoOrdenTrabajoCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.ordenId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.ordenId())));

        orden.cambiarEstado(command.nuevoEstado());
        ordenTrabajoRepository.save(orden);
    }
}