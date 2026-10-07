package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.EliminarItemOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EliminarItemOrdenTrabajoUseCase implements EliminarItemOrdenTrabajo {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final FacturaRepository facturaRepository;

    @Override
    public void eliminar(EliminarItemOrdenTrabajoCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.ordenId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.ordenId())));

        orden.eliminarItem(command.itemId());
        validarTotalCubreLoFacturado(orden);
        ordenTrabajoRepository.save(orden);
    }

    private void validarTotalCubreLoFacturado(OrdenTrabajo orden) {
        List<Factura> facturasActivas = facturaRepository.findActivasByOrdenTrabajoId(orden.getId());
        if (!facturasActivas.isEmpty()) {
            orden.validarTotalCubre(Factura.sumarMontoFacturado(facturasActivas));
        }
    }
}