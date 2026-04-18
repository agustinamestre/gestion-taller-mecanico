package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.RegistrarModeloCommand;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.model.Modelo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ModeloApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "nombre", source = "command.nombre")
    @Mapping(target = "marca", source = "marca")
    Modelo commandToDomain(RegistrarModeloCommand command, Marca marca);
}
