package com.taller.gestion_taller.application.dto;

import java.math.BigDecimal;

public record DashboardResumen(
        long ordenesEnCurso,
        long ordenesListasParaEntregar,
        long presupuestosPendientes,
        long presupuestosAprobadosSinOrden,
        BigDecimal montoFacturadoUltimoMes,
        long alertasPendientes
) {}
