package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.persistence.entity.VehiculoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehiculoPersistenceMapper {

    @Mapping(source = "modelo", target = "modelo")
    @Mapping(source = "cliente", target = "cliente")
    @Mapping(source = "activo", target = "activo")
    Vehiculo toDomain(VehiculoEntity vehiculoEntity);
    
    @Mapping(source = "modelo", target = "modelo")
    @Mapping(source = "cliente", target = "cliente")
    @Mapping(source = "activo", target = "activo")
    VehiculoEntity toEntity(Vehiculo vehiculo);
}
