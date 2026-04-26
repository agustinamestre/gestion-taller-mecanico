package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.dto.ClienteSummaryResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehiculoRestMapper {

    RegistrarVehiculoCommand requestToCommand(VehiculoRequest request);

    @Mapping(source = "modelo.marca.nombre", target = "marca")
    @Mapping(source = "modelo.nombre", target = "modelo")
    @Mapping(source = "cliente", target = "cliente")
    VehiculoResponse domainToResponse(Vehiculo vehiculo);

    ClienteSummaryResponse clienteToClienteSummaryResponse(Cliente cliente);
}
