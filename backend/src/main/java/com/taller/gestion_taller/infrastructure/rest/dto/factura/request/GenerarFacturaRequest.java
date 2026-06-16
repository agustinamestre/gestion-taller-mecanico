package com.taller.gestion_taller.infrastructure.rest.dto.factura.request;

import com.taller.gestion_taller.infrastructure.rest.validation.formaPago.FormaPagoValida;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
