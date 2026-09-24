package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.ConsultarFacturasCommand;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ConsultarFacturasUseCase implements ConsultarFacturas {

    private final FacturaRepository facturaRepository;

    @Override
    public List<Factura> consultar(ConsultarFacturasCommand command) {
        return facturaRepository.findByFiltros(
                command.getId(),
                command.getNumeroFactura(),
                command.getClienteDni()
        );
    }
}