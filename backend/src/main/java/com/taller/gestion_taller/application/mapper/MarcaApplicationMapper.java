package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.RegistrarMarcaCommand;
import com.taller.gestion_taller.domain.model.Marca;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MarcaApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Marca commandToDomain(RegistrarMarcaCommand command);
}
