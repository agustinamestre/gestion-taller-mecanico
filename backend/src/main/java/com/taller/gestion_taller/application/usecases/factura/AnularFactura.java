package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.AnularFacturaCommand;
import com.taller.gestion_taller.domain.model.Factura;

public interface AnularFactura {
    Factura anularFactura(AnularFacturaCommand command);
}
