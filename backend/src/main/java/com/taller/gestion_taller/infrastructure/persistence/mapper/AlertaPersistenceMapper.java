package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.infrastructure.persistence.entity.AlertaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface AlertaPersistenceMapper {

    Alerta toDomain(AlertaEntity entity);

    AlertaEntity toEntity(Alerta alerta);

    void updateEntity(Alerta alerta, @MappingTarget AlertaEntity entity);
}
