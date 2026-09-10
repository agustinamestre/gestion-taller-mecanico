package com.taller.gestion_taller.infrastructure.rest.dto.alerta.response;

import java.time.LocalDate;

public record AlertaResponse(
        Long id,
        Long vehiculoId,
        String patenteVehiculo,
        String nombreCliente,
        LocalDate fechaAlerta,
        String tipo,
        boolean contactado,
        LocalDate fechaContacto,
        String medioContacto,
        String observaciones
) {}
