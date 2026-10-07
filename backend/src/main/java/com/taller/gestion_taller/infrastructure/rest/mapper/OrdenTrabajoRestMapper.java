package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.orden.*;
import com.taller.gestion_taller.domain.model.ItemOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.*;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.ItemOrdenTrabajoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.OrdenTrabajoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {PresupuestoRestMapper.class})
public interface OrdenTrabajoRestMapper {

    RegistrarOrdenTrabajoCommand requestToCommand(RegistrarOrdenTrabajoRequest request, Long usuarioCreacionId);
    ModificarOrdenTrabajoCommand toModificarCommand(Long ordenId, ModificarOrdenTrabajoRequest request);
    CambiarEstadoOrdenTrabajoCommand toCambiarEstadoCommand(Long ordenId, CambiarEstadoOrdenTrabajoRequest request);
    AgregarItemOrdenTrabajoCommand toAgregarItemCommand(Long ordenId, AgregarItemOrdenTrabajoRequest request);
    ModificarItemOrdenTrabajoCommand toModificarItemCommand(Long ordenId, Long itemId, ModificarItemOrdenTrabajoRequest request);
    EliminarItemOrdenTrabajoCommand toEliminarItemCommand(Long ordenId, Long itemId);

    @Mapping(source = "ordenTrabajo.vehiculo.patente", target = "patenteVehiculo")
    @Mapping(source = "ordenTrabajo.presupuesto.id", target = "presupuestoId")
    @Mapping(source = "ordenTrabajo.presupuesto.items", target = "itemsPresupuesto")
    @Mapping(source = "ordenTrabajo.items", target = "itemsOrden")
    @Mapping(target = "total", expression = "java(ordenTrabajo.calcularTotal())")
    @Mapping(source = "facturada", target = "facturada")
    @Mapping(source = "ordenTrabajo.vehiculo.cliente.nombre", target = "nombreCliente")
    @Mapping(source = "ordenTrabajo.vehiculo.cliente.apellido", target = "apellidoCliente")
    @Mapping(source = "ordenTrabajo.vehiculo.cliente.telefono", target = "telefonoCliente")
    OrdenTrabajoResponse domainToResponse(OrdenTrabajo ordenTrabajo, boolean facturada);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(target = "subtotal", expression = "java(item.calcularSubtotal())")
    ItemOrdenTrabajoResponse itemDomainToResponse(ItemOrdenTrabajo item);
}