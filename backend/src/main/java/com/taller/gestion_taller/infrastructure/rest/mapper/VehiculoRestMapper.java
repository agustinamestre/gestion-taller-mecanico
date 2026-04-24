package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehiculoRestMapper {

    RegistrarVehiculoCommand requestToCommand(VehiculoRequest request);

    @Mapping(target = "modelo.nombre", source = "modelo.nombre")
    @Mapping(target = "modelo.marca.nombre", source = "modelo.marca.nombre")
    @Mapping(target = "cliente.dni", source = "cliente.dni")
    @Mapping(target = "cliente.nombre", source = "cliente.nombre")
    @Mapping(target = "cliente.apellido", source = "cliente.apellido")
    VehiculoResponse domainToResponse(Vehiculo vehiculo);
}
