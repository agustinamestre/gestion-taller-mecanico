package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.GenerarFacturaCommand;
import com.taller.gestion_taller.application.usecases.factura.GenerarFactura;
import com.taller.gestion_taller.domain.model.Factura;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final GenerarFactura generarFacturaUseCase;

    @Transactional
    public Factura generarFactura(GenerarFacturaCommand command) {
        return generarFacturaUseCase.generarFactura(command);
    }
}
