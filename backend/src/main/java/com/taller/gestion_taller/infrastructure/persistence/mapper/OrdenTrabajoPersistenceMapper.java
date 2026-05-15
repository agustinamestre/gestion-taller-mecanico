package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.persistence.entity.OrdenTrabajoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehiculoPersistenceMapper.class, PresupuestoPersistenceMapper.class})
public interface OrdenTrabajoPersistenceMapper {

    @Mapping(source = "vehiculo", target = "vehiculo")
    @Mapping(source = "presupuesto", target = "presupuesto")
    OrdenTrabajoEntity toEntity(OrdenTrabajo ordenTrabajo);

    @Mapping(source = "vehiculo", target = "vehiculo")
    @Mapping(source = "presupuesto", target = "presupuesto")
    OrdenTrabajo toDomain(OrdenTrabajoEntity entity);
}
