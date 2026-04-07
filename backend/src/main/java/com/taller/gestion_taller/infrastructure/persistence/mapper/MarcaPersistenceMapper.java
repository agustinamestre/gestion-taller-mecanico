package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.infrastructure.persistence.entity.MarcaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarcaPersistenceMapper {

    MarcaEntity toEntity(Marca marca);
    Marca toDomain(MarcaEntity entity);
}
