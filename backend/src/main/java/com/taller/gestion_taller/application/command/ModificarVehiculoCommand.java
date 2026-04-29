package com.taller.gestion_taller.application.command;

public record ModificarVehiculoCommand(
        Long modeloId,
        Integer anio,
        Long clienteId
) {
}
