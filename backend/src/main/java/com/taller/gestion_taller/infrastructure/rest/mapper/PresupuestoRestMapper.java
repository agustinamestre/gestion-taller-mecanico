package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.infrastructure.rest.dto.PresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.PresupuestoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PresupuestoRestMapper {

    RegistrarPresupuestoCommand requestToCommand(PresupuestoRequest request);

    @Mapping(source = "vehiculo.patente", target = "patenteVehiculo")
    PresupuestoResponse domainToResponse(Presupuesto presupuesto);
}
