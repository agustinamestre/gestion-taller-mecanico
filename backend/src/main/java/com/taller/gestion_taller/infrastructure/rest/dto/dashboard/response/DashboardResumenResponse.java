package com.taller.gestion_taller.infrastructure.rest.dto.dashboard.response;

import java.math.BigDecimal;

public record DashboardResumenResponse(
        long ordenesEnCurso,
        long ordenesListasParaEntregar,
        long presupuestosPendientes,
        long presupuestosAprobadosSinOrden,
        BigDecimal montoFacturadoUltimoMes,
        long alertasPendientes
) {}
