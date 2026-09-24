package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.TipoComprobante;
import com.taller.gestion_taller.domain.repositories.ContadorFacturaRepository;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.service.FacturaValidator;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GenerarFacturaUseCase implements GenerarFactura {

    private static final String FORMATO_NUMERO_FACTURA = "F%08d";

    // Facturación asume taller monotributista -> siempre Factura C
    private static final TipoComprobante TIPO_COMPROBANTE = TipoComprobante.C;

    private final FacturaRepository facturaRepository;
    private final FacturaValidator facturaValidator;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final ContadorFacturaRepository contadorFacturaRepository;

    @Override
    public Factura generarFactura(GenerarFacturaCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.getOrdenTrabajoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.getOrdenTrabajoId())));

        facturaValidator.validarOrdenParaFacturacion(orden);

        String numeroFactura = String.format(FORMATO_NUMERO_FACTURA, contadorFacturaRepository.siguienteNumero());
        Factura factura = Factura.crearNueva(orden, command.getFormaPago(), numeroFactura, TIPO_COMPROBANTE);
        Factura guardada = facturaRepository.save(factura);

        orden.marcarComoFacturada();
        ordenTrabajoRepository.save(orden);

        return guardada;
    }

}
