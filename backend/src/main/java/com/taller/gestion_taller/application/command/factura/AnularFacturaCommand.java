package com.taller.gestion_taller.application.command.factura;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnularFacturaCommand {
    private Long facturaId;
    private String motivo;
}
