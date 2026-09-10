package com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.response;

import com.taller.gestion_taller.infrastructure.rest.dto.cliente.response.ClienteSummaryResponse;

import java.time.LocalDate;

public record VehiculoResponse (
        Long id,
        String patente,
        String marca,
        String modelo,
        Long modeloId,
        Integer anio,
        Integer kilometrajeActual,
        ClienteSummaryResponse cliente,
        boolean activo,
        LocalDate fechaUltimoService
) { }
