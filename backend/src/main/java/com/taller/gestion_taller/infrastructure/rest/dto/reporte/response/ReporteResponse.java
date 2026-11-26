package com.taller.gestion_taller.infrastructure.rest.dto.reporte.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReporteResponse(
        LocalDate desde,
        LocalDate hasta,
        String agrupacion,
        BigDecimal totalFacturado,
        long ordenesIngresadas,
        long ordenesEntregadas,
        Double tiempoPromedioReparacionDias,
        List<PuntoFacturacionResponse> facturacion,
        List<PuntoOrdenesResponse> ordenes,
        List<ServicioTopResponse> topServicios,
        List<FacturacionFormaPagoResponse> formasPago
) {

    public record PuntoFacturacionResponse(LocalDate fecha, BigDecimal monto) {}

    public record PuntoOrdenesResponse(LocalDate fecha, long ingresadas, long entregadas) {}

    public record ServicioTopResponse(Long productoId, String nombre, long cantidadOrdenes) {}

    public record FacturacionFormaPagoResponse(String formaPago, BigDecimal monto, long cantidad) {}
}
