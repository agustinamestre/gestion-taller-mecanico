package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.infrastructure.persistence.entity.ProductoEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ProductoPersistenceMapper {
    ProductoEntity toEntity(Producto domain);
    Producto toDomain(ProductoEntity entity);
}
