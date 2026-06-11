package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.AsociarVehiculoAPresupuestoCommand;
import com.taller.gestion_taller.domain.model.Presupuesto;

public interface AsociarVehiculoAPresupuesto {
    Presupuesto asociar(AsociarVehiculoAPresupuestoCommand command);
}
