package com.taller.gestion_taller.application.command;

public record RegistrarPresupuestoCommand(
        Long vehiculoId,
        String observaciones
) {
}
