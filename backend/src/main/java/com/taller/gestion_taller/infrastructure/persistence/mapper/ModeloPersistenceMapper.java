package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.infrastructure.persistence.entity.ModeloEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ModeloPersistenceMapper {

    ModeloEntity toEntity(Modelo modelo);
    Modelo toDomain(ModeloEntity entity);
}
