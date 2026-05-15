package com.taller.gestion_taller.application.command.orden;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;

public record CambiarEstadoOrdenTrabajoCommand(Long ordenId, EstadoOrdenTrabajo nuevoEstado) {}