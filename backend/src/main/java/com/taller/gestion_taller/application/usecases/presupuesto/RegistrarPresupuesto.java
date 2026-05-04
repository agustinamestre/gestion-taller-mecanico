package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.model.Presupuesto;

public interface RegistrarPresupuesto {
    Presupuesto registrar(RegistrarPresupuestoCommand command);
}
