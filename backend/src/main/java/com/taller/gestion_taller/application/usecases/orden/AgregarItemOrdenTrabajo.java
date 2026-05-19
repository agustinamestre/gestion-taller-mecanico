package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.AgregarItemOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;

public interface AgregarItemOrdenTrabajo {
    OrdenTrabajo agregar(AgregarItemOrdenTrabajoCommand command);
}
