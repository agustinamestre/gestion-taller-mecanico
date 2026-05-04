package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.infrastructure.persistence.entity.PresupuestoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehiculoPersistenceMapper.class})
public interface PresupuestoPersistenceMapper {

    @Mapping(source = "vehiculo", target = "vehiculo")
    Presupuesto toDomain(PresupuestoEntity entity);

    @Mapping(source = "vehiculo", target = "vehiculo")
    PresupuestoEntity toEntity(Presupuesto presupuesto);
}
