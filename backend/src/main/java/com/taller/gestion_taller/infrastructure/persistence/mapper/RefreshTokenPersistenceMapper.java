package com.taller.gestion_taller.infrastructure.persistence.mapper;

import com.taller.gestion_taller.domain.model.RefreshToken;
import com.taller.gestion_taller.infrastructure.persistence.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;

@Mapper
public interface RefreshTokenPersistenceMapper {

    RefreshTokenEntity toEntity(RefreshToken refreshToken);

    default RefreshToken toDomain(RefreshTokenEntity entity) {
        if (entity == null) return null;
        return RefreshToken.reconstruir(
                entity.getId(), entity.getUsuarioId(), entity.getTokenHash(),
                entity.getFechaExpiracion(), entity.isRevocado()
        );
    }
}