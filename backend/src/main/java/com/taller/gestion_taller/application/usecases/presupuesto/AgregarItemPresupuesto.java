package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.model.Presupuesto;

public interface AgregarItemPresupuesto {
    Presupuesto agregar(AgregarItemPresupuestoCommand command);
}
