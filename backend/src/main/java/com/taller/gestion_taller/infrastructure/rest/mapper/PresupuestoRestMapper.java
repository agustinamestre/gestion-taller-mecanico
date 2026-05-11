package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.presupuesto.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.EliminarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.ModificarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.model.ItemPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.ItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.ItemPresupuestoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.PresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.PresupuestoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.ModificarItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.PresupuestoSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PresupuestoRestMapper {

    RegistrarPresupuestoCommand requestToCommand(PresupuestoRequest request);

    @Mapping(target = "presupuestoId", source = "presupuestoId")
    AgregarItemPresupuestoCommand requestToAgregarItemCommand(Long presupuestoId, ItemPresupuestoRequest request);

    @Mapping(source = "vehiculo.patente", target = "patenteVehiculo")
    @Mapping(source = "items", target = "items")
    @Mapping(target = "total", expression = "java(presupuesto.calcularTotal())")
    PresupuestoResponse domainToResponse(Presupuesto presupuesto);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(target = "subtotal", expression = "java(item.calcularSubtotal())")
    ItemPresupuestoResponse itemDomainToResponse(ItemPresupuesto item);

    @Mapping(target = "presupuestoId", source = "presupuestoId")
    @Mapping(target = "itemId", source = "itemId")
    ModificarItemPresupuestoCommand requestToModificarItemCommand(
            Long presupuestoId,
            Long itemId,
            ModificarItemPresupuestoRequest request
    );

    @Mapping(target = "total", expression = "java(presupuesto.calcularTotal())")
    PresupuestoSummaryResponse domainToResumenResponse(Presupuesto presupuesto);

    EliminarItemPresupuestoCommand toEliminarItemCommand(Long presupuestoId, Long itemId);
}
