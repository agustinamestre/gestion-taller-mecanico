package com.taller.gestion_taller.application.command.vehiculo;

import java.time.LocalDate;

public record ModificarVehiculoCommand(
        Long modeloId,
        Integer anio,
        Long clienteId,
        LocalDate fechaUltimoService
) {
}
