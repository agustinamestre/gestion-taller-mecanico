package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.EliminarItemOrdenTrabajoCommand;

public interface EliminarItemOrdenTrabajo {
    void eliminar(EliminarItemOrdenTrabajoCommand command);
}
