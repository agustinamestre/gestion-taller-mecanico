package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.ModificarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.model.Presupuesto;

public interface ModificarItemPresupuesto {
    Presupuesto modificar(ModificarItemPresupuestoCommand command);
}
