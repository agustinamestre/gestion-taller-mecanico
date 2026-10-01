package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.model.RefreshToken;
import com.taller.gestion_taller.domain.repositories.RefreshTokenRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.RefreshTokenEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.RefreshTokenPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final RefreshTokenPersistenceMapper mapper;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = mapper.toEntity(refreshToken);
        return mapper.toDomain(jpaRefreshTokenRepository.save(entity));
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return jpaRefreshTokenRepository.findByTokenHash(tokenHash).map(mapper::toDomain);
    }

    @Override
    public void revocarTodosPorUsuarioId(Long usuarioId) {
        jpaRefreshTokenRepository.revocarTodosPorUsuarioId(usuarioId);
    }
}