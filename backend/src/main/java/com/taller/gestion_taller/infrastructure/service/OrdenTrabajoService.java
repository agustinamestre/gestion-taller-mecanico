package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.application.usecases.orden.RegistrarOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {

    private final RegistrarOrdenTrabajo registrarOrdenTrabajoUseCase;

    @Transactional
    public OrdenTrabajo registrarOrden(RegistrarOrdenTrabajoCommand command) {
        return registrarOrdenTrabajoUseCase.registrar(command);
    }
}