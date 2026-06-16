package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.model.Factura;

public interface GenerarFactura {
    Factura generarFactura(GenerarFacturaCommand command);
}
