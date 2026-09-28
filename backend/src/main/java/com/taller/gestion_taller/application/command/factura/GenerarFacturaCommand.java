package com.taller.gestion_taller.application.command.factura;

import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.model.TipoFactura;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerarFacturaCommand {
    private Long ordenTrabajoId;
    private FormaPago formaPago;
    private TipoFactura tipoFactura;
    private BigDecimal monto;
}
