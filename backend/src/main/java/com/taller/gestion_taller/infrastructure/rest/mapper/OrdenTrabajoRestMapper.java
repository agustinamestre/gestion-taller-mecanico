package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.OrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.OrdenTrabajoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrdenTrabajoRestMapper {

    RegistrarOrdenTrabajoCommand requestToCommand(OrdenTrabajoRequest request);

    @Mapping(source = "vehiculo.patente", target = "patenteVehiculo")
    @Mapping(source = "presupuesto.id", target = "presupuestoId")
    OrdenTrabajoResponse domainToResponse(OrdenTrabajo ordenTrabajo);
}
