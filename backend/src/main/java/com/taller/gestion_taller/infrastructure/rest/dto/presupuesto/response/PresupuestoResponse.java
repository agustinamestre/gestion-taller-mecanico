package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PresupuestoResponse (
         Long id,
         String patenteVehiculo,
         LocalDate fechaEmision,
         LocalDate fechaVencimiento,
         String estado,
         String observaciones,
         List<ItemPresupuestoResponse> items,
         BigDecimal total
) { }
