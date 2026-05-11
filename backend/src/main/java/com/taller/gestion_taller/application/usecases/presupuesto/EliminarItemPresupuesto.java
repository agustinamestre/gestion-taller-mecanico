package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.EliminarItemPresupuestoCommand;

public interface EliminarItemPresupuesto {
    void eliminar(EliminarItemPresupuestoCommand command);
}
