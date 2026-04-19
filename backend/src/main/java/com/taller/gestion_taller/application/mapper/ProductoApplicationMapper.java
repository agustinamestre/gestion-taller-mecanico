package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductoApplicationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombre",source = "command.nombre")
    @Mapping(target = "activo", constant = "true")
    Producto commandToDomain(RegistrarProductoCommand command);
}
