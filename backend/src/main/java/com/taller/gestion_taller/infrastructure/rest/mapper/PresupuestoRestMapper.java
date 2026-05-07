package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.model.ItemPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.infrastructure.rest.dto.ItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ItemPresupuestoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.PresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.PresupuestoResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PresupuestoRestMapper {

    RegistrarPresupuestoCommand requestToCommand(PresupuestoRequest request);

    @Mapping(target = "presupuestoId", source = "presupuestoId")
    AgregarItemPresupuestoCommand requestToAgregarItemCommand(Long presupuestoId, ItemPresupuestoRequest request);

    @Mapping(source = "vehiculo.patente", target = "patenteVehiculo")
    @Mapping(source = "items", target = "items")
    PresupuestoResponse domainToResponse(Presupuesto presupuesto);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(target = "subtotal", ignore = true)
    ItemPresupuestoResponse itemDomainToResponse(ItemPresupuesto item);

    @AfterMapping
    default void calcularSubtotal(ItemPresupuesto item, @MappingTarget ItemPresupuestoResponse response) {
        response.setSubtotal(item.calcularSubtotal());
    }

    @AfterMapping
    default void calcularTotal(Presupuesto presupuesto, @MappingTarget PresupuestoResponse response) {
        response.setTotal(presupuesto.calcularTotal());
    }
}
