package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;

public interface CambiarEstadoOrdenTrabajo {
    void cambiar(CambiarEstadoOrdenTrabajoCommand command);
}
