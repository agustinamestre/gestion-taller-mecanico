package com.taller.gestion_taller.application.usecases.auth;

import com.taller.gestion_taller.domain.model.RefreshToken;
import com.taller.gestion_taller.domain.repositories.RefreshTokenRepository;
import com.taller.gestion_taller.infrastructure.persistence.refresh.RefreshTokenGenerator;
import com.taller.gestion_taller.infrastructure.security.refresh.RefreshTokenHasher;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class GenerarRefreshTokenUseCase implements GenerarRefreshToken {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenHasher refreshTokenHasher;
    private final long expiracionDias;

    @Override
    public String generar(Long usuarioId) {
        String tokenPlano = refreshTokenGenerator.generar();
        String tokenHash = refreshTokenHasher.hashear(tokenPlano);

        RefreshToken refreshToken = RefreshToken.crearNuevo(
                usuarioId,
                tokenHash,
                Instant.now().plus(expiracionDias, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(refreshToken);
        return tokenPlano;
    }
}