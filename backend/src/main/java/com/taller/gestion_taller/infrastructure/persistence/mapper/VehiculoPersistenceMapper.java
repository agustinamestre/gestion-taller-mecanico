package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.persistence.entity.VehiculoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehiculoPersistenceMapper {

    @Mapping(source = "modelo", target = "modelo")
    @Mapping(source = "cliente", target = "cliente")
    Vehiculo toDomain(VehiculoEntity vehiculoEntity);

    @Mapping(source = "modelo", target = "modelo")
    @Mapping(source = "cliente", target = "cliente")
    VehiculoEntity toEntity(Vehiculo vehiculo);
}
