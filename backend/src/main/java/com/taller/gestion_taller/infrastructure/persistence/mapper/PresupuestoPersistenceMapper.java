package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.infrastructure.persistence.entity.PresupuestoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {VehiculoPersistenceMapper.class, ItemPresupuestoPersistenceMapper.class})
public interface PresupuestoPersistenceMapper {

    @Mapping(source = "vehiculo", target = "vehiculo")
    @Mapping(source = "items", target = "items")
    Presupuesto toDomain(PresupuestoEntity entity);

    @Mapping(source = "vehiculo", target = "vehiculo")
    @Mapping(source  = "items", target = "items")
    PresupuestoEntity toEntity(Presupuesto presupuesto);

    @Mapping(source = "vehiculo", target = "vehiculo")
    @Mapping(source = "items", target = "items")
    void updateEntity(Presupuesto presupuesto, @MappingTarget PresupuestoEntity entity);

    @AfterMapping
    default void sincronizarRelacionItems(@MappingTarget PresupuestoEntity entity) {
        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setPresupuesto(entity));
        }
    }

}
