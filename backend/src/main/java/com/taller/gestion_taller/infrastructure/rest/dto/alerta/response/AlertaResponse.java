package com.taller.gestion_taller.infrastructure.rest.dto.alerta.response;

import java.time.LocalDate;

public record AlertaResponse(
        Long id,
        Long vehiculoId,
        String patenteVehiculo,
        String nombreCliente,
        String telefonoCliente,
        LocalDate fechaAlerta,
        String tipo,
        String motivo,
        boolean contactado,
        LocalDate fechaContacto,
        String medioContacto,
        String observaciones
) {}
