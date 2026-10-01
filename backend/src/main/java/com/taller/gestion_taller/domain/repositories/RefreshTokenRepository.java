package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    void revocarTodosPorUsuarioId(Long usuarioId);
}