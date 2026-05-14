package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.persistence.entity.OrdenTrabajoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrdenTrabajoPersistenceMapper {

    @Mapping(source = "vehiculo.id", target = "vehiculo.id")
    OrdenTrabajoEntity toEntity(OrdenTrabajo ordenTrabajo);

    OrdenTrabajo toDomain(OrdenTrabajoEntity entity);
}
