package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.CambiarEstadoPresupuestoCommand;

public interface CambiarEstadoPresupuesto {
    void cambiar(CambiarEstadoPresupuestoCommand command);
}
