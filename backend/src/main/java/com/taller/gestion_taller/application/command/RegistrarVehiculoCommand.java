package com.taller.gestion_taller.application.command;

public record RegistrarVehiculoCommand(
        String patente,
        Long modeloId,
        Integer anio,
        Long clienteId,
        Integer kilometrajeActual
) {
}
