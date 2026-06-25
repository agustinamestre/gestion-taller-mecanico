package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.infrastructure.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UsuarioPersistenceMapper {
    UsuarioEntity toEntity(Usuario usuario);
    Usuario toDomain(UsuarioEntity entity);
}