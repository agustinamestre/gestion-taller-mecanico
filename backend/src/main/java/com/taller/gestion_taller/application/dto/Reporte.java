package com.taller.gestion_taller.application.dto;

import com.taller.gestion_taller.domain.model.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record Reporte(
        LocalDate desde,
        LocalDate hasta,
        Agrupacion agrupacion,
        BigDecimal totalFacturado,
        long ordenesIngresadas,
        long ordenesEntregadas,
        Double tiempoPromedioReparacionDias,
        List<PuntoFacturacion> facturacion,
        List<PuntoOrdenes> ordenes,
        List<ServicioTop> topServicios,
        List<FacturacionFormaPago> formasPago
) {

    public enum Agrupacion { DIA, MES }

    public record PuntoFacturacion(LocalDate fecha, BigDecimal monto) {}

    public record PuntoOrdenes(LocalDate fecha, long ingresadas, long entregadas) {}

    public record ServicioTop(Long productoId, String nombre, long cantidadOrdenes) {}

    public record FacturacionFormaPago(FormaPago formaPago, BigDecimal monto, long cantidad) {}
}
