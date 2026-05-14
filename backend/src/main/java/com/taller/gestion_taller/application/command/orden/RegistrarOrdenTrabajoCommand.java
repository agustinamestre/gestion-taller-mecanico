package com.taller.gestion_taller.application.command.orden;

public record RegistrarOrdenTrabajoCommand(
        String patente,
        Long presupuestoId,
        String descripcionProblema,
        Long usuarioCreacionId
) {}