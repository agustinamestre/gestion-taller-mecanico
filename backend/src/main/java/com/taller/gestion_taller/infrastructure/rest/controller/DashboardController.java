package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.dto.DashboardResumen;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerDashboardController;
import com.taller.gestion_taller.infrastructure.rest.dto.dashboard.response.DashboardResumenResponse;
import com.taller.gestion_taller.infrastructure.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController implements SwaggerDashboardController {

    private final DashboardService dashboardService;

    @Override
    public ResponseEntity<DashboardResumenResponse> obtenerResumen() {
        DashboardResumen resumen = dashboardService.obtenerResumen();
        return ResponseEntity.ok(new DashboardResumenResponse(
                resumen.ordenesEnCurso(),
                resumen.ordenesListasParaEntregar(),
                resumen.presupuestosPendientes(),
                resumen.presupuestosAprobadosSinOrden(),
                resumen.montoFacturadoUltimoMes(),
                resumen.alertasPendientes()
        ));
    }
}
