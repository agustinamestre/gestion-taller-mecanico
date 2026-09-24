package com.taller.gestion_taller.application.command.factura;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultarFacturasCommand {
    private Long id;
    private String numeroFactura;
    private String clienteDni;
}