package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.usuario.RegistrarUsuarioCommand;
import com.taller.gestion_taller.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UsuarioApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Usuario commandToDomain(RegistrarUsuarioCommand command);
}