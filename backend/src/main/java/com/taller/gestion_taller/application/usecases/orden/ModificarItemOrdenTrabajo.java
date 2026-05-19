package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.ModificarItemOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;

public interface ModificarItemOrdenTrabajo {
    OrdenTrabajo modificar(ModificarItemOrdenTrabajoCommand command);
}
