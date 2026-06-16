package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.infrastructure.persistence.entity.FacturaEntity;
import org.mapstruct.Mapper;

@Mapper(uses = {OrdenTrabajoPersistenceMapper.class})
public interface FacturaPersistenceMapper {

    FacturaEntity toEntity(Factura factura);

    Factura toDomain(FacturaEntity entity);
}
