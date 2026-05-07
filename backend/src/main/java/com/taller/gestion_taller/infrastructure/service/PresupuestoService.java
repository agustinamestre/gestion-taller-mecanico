package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.application.usecases.presupuesto.AgregarItemPresupuesto;
import com.taller.gestion_taller.application.usecases.presupuesto.RegistrarPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PresupuestoService {

    private final RegistrarPresupuesto registrarPresupuestoUseCase;
    private final AgregarItemPresupuesto agregarItemPresupuestoUseCase;

    @Transactional
    public Presupuesto registrarPresupuesto(RegistrarPresupuestoCommand command) {
        return registrarPresupuestoUseCase.registrar(command);
    }

    @Transactional
    public Presupuesto agregarItem(AgregarItemPresupuestoCommand command) {
        return agregarItemPresupuestoUseCase.agregar(command);
    }
}
