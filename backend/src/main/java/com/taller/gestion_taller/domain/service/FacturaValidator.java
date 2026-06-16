package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacturaValidator {

    private final FacturaRepository facturaRepository;

    public void validarOrdenParaFacturacion(OrdenTrabajo orden) {
        if (orden.getEstado() != EstadoOrdenTrabajo.FINALIZADO
                && orden.getEstado() != EstadoOrdenTrabajo.ENTREGADO) {
            throw new BusinessRunTimeException(
                    BusinessErrors.ordenNoFacturable(orden.getEstado()));
        }

        facturaRepository.findByOrdenTrabajoId(orden.getId()).ifPresent(f -> {
            throw new BusinessRunTimeException(
                    BusinessErrors.ordenYaFacturada(orden.getId()));
        });
    }
}
