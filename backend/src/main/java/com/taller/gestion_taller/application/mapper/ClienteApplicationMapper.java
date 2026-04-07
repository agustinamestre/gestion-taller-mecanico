package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.RegistrarClienteCommand;
import com.taller.gestion_taller.domain.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClienteApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    Cliente commandToDomain(RegistrarClienteCommand command);
}
