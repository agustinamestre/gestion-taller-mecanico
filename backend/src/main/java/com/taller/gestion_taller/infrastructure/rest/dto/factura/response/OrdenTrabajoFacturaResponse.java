package com.taller.gestion_taller.infrastructure.rest.dto.factura.response;

import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.ItemOrdenTrabajoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.ItemPresupuestoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrdenTrabajoFacturaResponse(
        Long id,
        String patenteVehiculo,
        LocalDate fechaIngreso,
        LocalDate fechaEgreso,
        String descripcionProblema,
        String estado,
        List<ItemPresupuestoResponse> itemsPresupuesto,
        List<ItemOrdenTrabajoResponse> itemsOrden,
        BigDecimal total
) {}