package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PresupuestoSummaryResponse(
        Long id,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        String estado,
        BigDecimal total
) {}