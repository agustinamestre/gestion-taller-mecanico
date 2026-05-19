package com.taller.gestion_taller.infrastructure.rest.dto.orden.response;

import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.ItemPresupuestoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrdenTrabajoResponse(
        Long id,
        String patenteVehiculo,
        Long presupuestoId,
        LocalDate fechaIngreso,
        LocalDate fechaEgreso,
        String descripcionProblema,
        String estado,
        Long usuarioCreacionId,
        List<ItemPresupuestoResponse> itemsPresupuesto,
        List<ItemOrdenTrabajoResponse> itemsOrden,
        BigDecimal total
) {}
