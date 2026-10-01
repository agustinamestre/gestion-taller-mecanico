package com.taller.gestion_taller.application.usecases.auth;

import com.taller.gestion_taller.domain.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RevocarRefreshTokenUseCase implements RevocarRefreshToken {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void revocar(Long usuarioId) {
        refreshTokenRepository.revocarTodosPorUsuarioId(usuarioId);
    }
}