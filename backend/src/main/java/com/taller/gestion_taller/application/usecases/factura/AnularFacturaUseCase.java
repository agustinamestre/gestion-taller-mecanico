package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.AnularFacturaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.service.FacturaValidator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AnularFacturaUseCase implements AnularFactura {

    private final FacturaRepository facturaRepository;
    private final FacturaValidator facturaValidator;

    @Override
    public Factura anularFactura(AnularFacturaCommand command) {
        Factura factura = facturaRepository.findById(command.getFacturaId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.facturaNoEncontrada(command.getFacturaId())));

        List<Factura> facturasActivasDeLaOrden = facturaRepository
                .findByFiltros(null, null, null, null, factura.getOrdenTrabajo().getId(), null, null)
                .stream()
                .filter(f -> f.getEstado() == EstadoFactura.EMITIDA)
                .toList();

        facturaValidator.validarAnulacion(factura, facturasActivasDeLaOrden);

        factura.anular(command.getMotivo());

        return facturaRepository.save(factura);
    }
}