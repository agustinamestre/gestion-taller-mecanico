package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.dto.Reporte;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerReporteController;
import com.taller.gestion_taller.infrastructure.rest.dto.reporte.response.ReporteResponse;
import com.taller.gestion_taller.infrastructure.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController implements SwaggerReporteController {

    private final ReporteService reporteService;

    @Override
    public ResponseEntity<ReporteResponse> obtener(LocalDate desde, LocalDate hasta) {
        Reporte reporte = reporteService.obtener(desde, hasta);
        return ResponseEntity.ok(new ReporteResponse(
                reporte.desde(),
                reporte.hasta(),
                reporte.agrupacion().name(),
                reporte.totalFacturado(),
                reporte.ordenesIngresadas(),
                reporte.ordenesEntregadas(),
                reporte.tiempoPromedioReparacionDias(),
                reporte.facturacion().stream()
                        .map(p -> new ReporteResponse.PuntoFacturacionResponse(p.fecha(), p.monto())).toList(),
                reporte.ordenes().stream()
                        .map(p -> new ReporteResponse.PuntoOrdenesResponse(p.fecha(), p.ingresadas(), p.entregadas())).toList(),
                reporte.topServicios().stream()
                        .map(s -> new ReporteResponse.ServicioTopResponse(s.productoId(), s.nombre(), s.cantidadOrdenes())).toList(),
                reporte.formasPago().stream()
                        .map(f -> new ReporteResponse.FacturacionFormaPagoResponse(f.formaPago().name(), f.monto(), f.cantidad())).toList()));
    }
}
