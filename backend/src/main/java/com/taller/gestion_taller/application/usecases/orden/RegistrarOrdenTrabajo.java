package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;

public interface RegistrarOrdenTrabajo {
    OrdenTrabajo registrar(RegistrarOrdenTrabajoCommand command);
}