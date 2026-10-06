package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Factura {

    private Long id;
    private OrdenTrabajo ordenTrabajo;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private FormaPago formaPago;
    private EstadoFactura estado;
    private String motivoAnulacion;
    private TipoComprobante tipoComprobante;
    private TipoFactura tipoFactura;
    private BigDecimal montoFacturado;

    public static Factura crearNueva(OrdenTrabajo ordenTrabajo, FormaPago formaPago, String numeroFactura,
                                      TipoComprobante tipoComprobante, TipoFactura tipoFactura, BigDecimal monto) {
        return Factura.builder()
                .ordenTrabajo(ordenTrabajo)
                .formaPago(formaPago)
                .numeroFactura(numeroFactura)
                .tipoComprobante(tipoComprobante)
                .tipoFactura(tipoFactura)
                .montoFacturado(monto)
                .fechaEmision(LocalDate.now())
                .estado(EstadoFactura.EMITIDA)
                .build();
    }

    public static BigDecimal sumarMontoFacturado(List<Factura> facturasActivas) {
        return facturasActivas.stream()
                .map(Factura::getMontoFacturado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calcularSaldoPendiente(BigDecimal totalOrden, List<Factura> facturasActivas) {
        return totalOrden.subtract(sumarMontoFacturado(facturasActivas));
    }

    public static boolean estaTotalmenteFacturada(BigDecimal totalOrden, List<Factura> facturasActivas) {
        if (totalOrden.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return sumarMontoFacturado(facturasActivas).compareTo(totalOrden) >= 0;
    }

    public String getClienteDni() {
        return ordenTrabajo.getVehiculo().getCliente().getDni();
    }

    public BigDecimal getTotal() {
        return ordenTrabajo.calcularTotal();
    }
}
