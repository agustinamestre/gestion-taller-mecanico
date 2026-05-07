package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.ItemPresupuesto;
import com.taller.gestion_taller.infrastructure.persistence.entity.ItemPresupuestoEntity;
import com.taller.gestion_taller.infrastructure.persistence.entity.ProductoEntity;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaProductoRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {ProductoPersistenceMapper.class})
public abstract class ItemPresupuestoPersistenceMapper {

    @Mapping(source = "producto", target = "producto")
    @Mapping(source = "presupuesto.id", target = "presupuestoId")
    public abstract ItemPresupuesto toDomain(ItemPresupuestoEntity entity);

    @Mapping(source = "producto", target = "producto")
    @Mapping(target = "presupuesto", ignore = true)
    public abstract ItemPresupuestoEntity toEntity(ItemPresupuesto domain);

    @Mapping(source = "producto", target = "producto")
    @Mapping(target = "presupuesto", ignore = true)
    public abstract void updateEntity(ItemPresupuesto domain, @MappingTarget ItemPresupuestoEntity entity);

}
