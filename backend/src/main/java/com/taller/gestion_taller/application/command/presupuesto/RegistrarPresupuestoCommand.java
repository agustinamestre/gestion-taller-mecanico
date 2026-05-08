package com.taller.gestion_taller.application.command.presupuesto;

public record RegistrarPresupuestoCommand(
        Long vehiculoId,
        String observaciones
) {
}
