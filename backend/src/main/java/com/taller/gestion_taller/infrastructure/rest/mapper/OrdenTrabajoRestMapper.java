package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;
import com.taller.gestion_taller.application.command.orden.ModificarOrdenTrabajoCommand;
import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.CambiarEstadoOrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.ModificarOrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.OrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.OrdenTrabajoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {PresupuestoRestMapper.class})
public interface OrdenTrabajoRestMapper {

    RegistrarOrdenTrabajoCommand requestToCommand(OrdenTrabajoRequest request);

    @Mapping(source = "vehiculo.patente", target = "patenteVehiculo")
    @Mapping(source = "presupuesto.id", target = "presupuestoId")
    @Mapping(source = "presupuesto.items", target = "items")
    OrdenTrabajoResponse domainToResponse(OrdenTrabajo ordenTrabajo);

    CambiarEstadoOrdenTrabajoCommand toCambiarEstadoCommand(Long ordenId, CambiarEstadoOrdenTrabajoRequest request);

    ModificarOrdenTrabajoCommand toModificarCommand(Long ordenId, ModificarOrdenTrabajoRequest request);
}
