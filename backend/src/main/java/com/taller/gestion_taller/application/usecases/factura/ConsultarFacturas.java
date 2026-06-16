package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.ConsultarFacturasCommand;
import com.taller.gestion_taller.domain.model.Factura;

import java.util.List;

public interface ConsultarFacturas {
    List<Factura> consultar(ConsultarFacturasCommand query);
}