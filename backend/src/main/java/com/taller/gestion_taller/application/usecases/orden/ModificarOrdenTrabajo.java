package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.ModificarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;

public interface ModificarOrdenTrabajo {
    OrdenTrabajo modificar(ModificarOrdenTrabajoCommand command);
}