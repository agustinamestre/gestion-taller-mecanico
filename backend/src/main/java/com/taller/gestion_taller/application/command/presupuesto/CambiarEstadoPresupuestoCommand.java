package com.taller.gestion_taller.application.command.presupuesto;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;

public record CambiarEstadoPresupuestoCommand(Long presupuestoId, EstadoPresupuesto nuevoEstado) {}