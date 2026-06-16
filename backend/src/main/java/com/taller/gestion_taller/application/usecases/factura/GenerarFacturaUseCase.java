package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.service.FacturaValidator;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GenerarFacturaUseCase implements GenerarFactura {

    private final FacturaRepository facturaRepository;
    private final FacturaValidator facturaValidator;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    @Override
    public Factura generarFactura(GenerarFacturaCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.getOrdenTrabajoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.getOrdenTrabajoId())));

        facturaValidator.validarOrdenParaFacturacion(orden);

        Factura factura = Factura.crearNueva(orden, command.getFormaPago());
        Factura guardada = facturaRepository.save(factura);

        String numeroFactura = String.format("F%08d", guardada.getId());
        return facturaRepository.actualizarNumeroFactura(guardada.getId(), numeroFactura);
    }

}
