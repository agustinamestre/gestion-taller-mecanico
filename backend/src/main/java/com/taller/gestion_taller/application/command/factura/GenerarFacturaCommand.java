package com.taller.gestion_taller.application.command.factura;

import com.taller.gestion_taller.domain.model.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerarFacturaCommand {
    private Long ordenTrabajoId;
    private FormaPago formaPago;
}
