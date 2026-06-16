package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Factura {

    private Long id;
    private OrdenTrabajo ordenTrabajo;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private FormaPago formaPago;

    public static Factura crearNueva(OrdenTrabajo ordenTrabajo, FormaPago formaPago) {
        return Factura.builder()
                .ordenTrabajo(ordenTrabajo)
                .formaPago(formaPago)
                .fechaEmision(LocalDate.now())
                .build();
    }

    public String getClienteDni() {
        return ordenTrabajo.getVehiculo().getCliente().getDni();
    }

    public BigDecimal getTotal() {
        return ordenTrabajo.calcularTotal();
    }
}
