package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.TipoFactura;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.request.GenerarFacturaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.response.FacturaResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.response.OrdenTrabajoFacturaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {OrdenTrabajoRestMapper.class, PresupuestoRestMapper.class})
public interface FacturaRestMapper {

    GenerarFacturaCommand requestToCommand(GenerarFacturaRequest request);

    @Mapping(source = "ordenTrabajo", target = "ordenTrabajo")
    @Mapping(target = "total", expression = "java(factura.getTotal())")
    @Mapping(target = "clienteDni", expression = "java(factura.getClienteDni())")
    FacturaResponse domainToResponse(Factura factura);

    @Mapping(source = "vehiculo.patente", target = "patenteVehiculo")
    @Mapping(source = "presupuesto.items", target = "itemsPresupuesto")
    @Mapping(source = "items", target = "itemsOrden")
    @Mapping(target = "total", expression = "java(ordenTrabajo.calcularTotal())")
    OrdenTrabajoFacturaResponse ordenToFacturaResponse(OrdenTrabajo ordenTrabajo);

    default FormaPago stringToFormaPago(String formaPago) {
        return FormaPago.valueOf(formaPago);
    }

    default TipoFactura stringToTipoFactura(String tipoFactura) {
        return TipoFactura.valueOf(tipoFactura);
    }
}
