package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.ConsultarFacturasCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ConsultarFacturasUseCase implements ConsultarFacturas {

    private final FacturaRepository facturaRepository;

    @Override
    public List<Factura> consultar(ConsultarFacturasCommand command) {
        validarRangoFechas(command.getFechaDesde(), command.getFechaHasta());

        return facturaRepository.findByFiltros(
                command.getId(),
                command.getNumeroFactura(),
                command.getClienteDni(),
                command.getFechaDesde(),
                command.getFechaHasta()
        );
    }

    private void validarRangoFechas(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new BusinessRunTimeException(
                    BusinessErrors.rangoFechasInvalido(desde, hasta));
        }
    }
}