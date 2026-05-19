package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.ItemOrdenTrabajo;
import com.taller.gestion_taller.infrastructure.persistence.entity.ItemOrdenTrabajoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ProductoPersistenceMapper.class})
public interface ItemOrdenTrabajoPersistenceMapper {

    @Mapping(source = "orden.id", target = "ordenId")
    @Mapping(source = "producto", target = "producto")
    ItemOrdenTrabajo toDomain(ItemOrdenTrabajoEntity entity);

    @Mapping(target = "orden", ignore = true)
    @Mapping(source = "producto", target = "producto")
    ItemOrdenTrabajoEntity toEntity(ItemOrdenTrabajo item);
}