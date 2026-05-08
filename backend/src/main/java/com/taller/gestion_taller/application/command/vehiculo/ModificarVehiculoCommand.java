package com.taller.gestion_taller.application.command.vehiculo;

public record ModificarVehiculoCommand(
        Long modeloId,
        Integer anio,
        Long clienteId
) {
}
