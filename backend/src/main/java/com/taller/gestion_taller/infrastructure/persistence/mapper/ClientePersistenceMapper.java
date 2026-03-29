package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.infrastructure.persistence.entity.ClienteEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ClientePersistenceMapper {

    ClienteEntity toEntity(Cliente cliente);

    Cliente toDomain(ClienteEntity entity);
}
