package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.request.GenerarFacturaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.response.FacturaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FacturaRestMapper {

    GenerarFacturaCommand requestToCommand(GenerarFacturaRequest request);

    @Mapping(source = "ordenTrabajo.id", target = "ordenTrabajoId")
    FacturaResponse domainToResponse(Factura factura);
}
