package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.factura.ConsultarFacturasCommand;
import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.application.usecases.factura.ConsultarFacturas;
import com.taller.gestion_taller.application.usecases.factura.GenerarFactura;
import com.taller.gestion_taller.domain.model.Factura;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final GenerarFactura generarFacturaUseCase;
    private final ConsultarFacturas consultarFacturasUseCase;

    @Transactional
    public Factura generarFactura(GenerarFacturaCommand command) {
        return generarFacturaUseCase.generarFactura(command);
    }

    @Transactional(readOnly = true)
    public List<Factura> consultarFacturas(ConsultarFacturasCommand command) {
        return consultarFacturasUseCase.consultar(command);
    }
}
