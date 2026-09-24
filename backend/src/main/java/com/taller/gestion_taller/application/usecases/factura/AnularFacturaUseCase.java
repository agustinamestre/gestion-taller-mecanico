package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.AnularFacturaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnularFacturaUseCase implements AnularFactura {

    private final FacturaRepository facturaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    @Override
    public Factura anularFactura(AnularFacturaCommand command) {
        Factura factura = facturaRepository.findById(command.getFacturaId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.facturaNoEncontrada(command.getFacturaId())));

        factura.anular(command.getMotivo());
        Factura anulada = facturaRepository.save(factura);

        OrdenTrabajo orden = factura.getOrdenTrabajo();
        orden.desmarcarFacturada();
        ordenTrabajoRepository.save(orden);

        return anulada;
    }
}
