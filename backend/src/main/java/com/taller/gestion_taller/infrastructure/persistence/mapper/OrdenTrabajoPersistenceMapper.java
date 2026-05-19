package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.persistence.entity.OrdenTrabajoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehiculoPersistenceMapper.class, PresupuestoPersistenceMapper.class, ItemOrdenTrabajoPersistenceMapper.class})
public interface OrdenTrabajoPersistenceMapper {

    @Mapping(source = "vehiculo", target = "vehiculo")
    @Mapping(source = "presupuesto", target = "presupuesto")
    @Mapping(source = "items", target = "items")
    OrdenTrabajoEntity toEntity(OrdenTrabajo ordenTrabajo);

    @Mapping(source = "vehiculo", target = "vehiculo")
    @Mapping(source = "presupuesto", target = "presupuesto")
    @Mapping(source = "items", target = "items")
    OrdenTrabajo toDomain(OrdenTrabajoEntity entity);
}