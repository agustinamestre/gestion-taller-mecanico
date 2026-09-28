package com.taller.gestion_taller.infrastructure.rest.dto.factura.request;

import com.taller.gestion_taller.infrastructure.rest.validation.formaPago.FormaPagoValida;
import com.taller.gestion_taller.infrastructure.rest.validation.tipoFactura.TipoFacturaValida;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerarFacturaRequest {

    @NotNull(message = "El ID de la orden de trabajo no puede ser nulo.")
    private Long ordenTrabajoId;

    @NotNull(message = "La forma de pago no puede ser nula.")
    @FormaPagoValida
    private String formaPago;

    @NotNull(message = "El tipo de factura no puede ser nulo.")
    @TipoFacturaValida
    private String tipoFactura;

    @NotNull(message = "El monto no puede ser nulo.")
    @Positive(message = "El monto debe ser mayor a cero.")
    private BigDecimal monto;
}
