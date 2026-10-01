package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.auth.*;
import com.taller.gestion_taller.domain.repositories.RefreshTokenRepository;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import com.taller.gestion_taller.infrastructure.persistence.refresh.RefreshTokenGenerator;
import com.taller.gestion_taller.infrastructure.security.jwt.JwtService;
import com.taller.gestion_taller.infrastructure.security.refresh.RefreshTokenHasher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthBeanConfiguration {

    @Bean
    public GenerarRefreshToken generarRefreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            RefreshTokenGenerator refreshTokenGenerator,
            RefreshTokenHasher refreshTokenHasher,
            @Value("${jwt.refresh.expiration-days}") long expiracionDias) {
        return new GenerarRefreshTokenUseCase(
                refreshTokenRepository, refreshTokenGenerator, refreshTokenHasher, expiracionDias);
    }

    @Bean
    public RefrescarToken refrescarTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            UsuarioRepository usuarioRepository,
            RefreshTokenHasher refreshTokenHasher,
            GenerarRefreshToken generarRefreshToken,
            JwtService jwtService) {
        return new RefrescarTokenUseCase(
                refreshTokenRepository, usuarioRepository, refreshTokenHasher, generarRefreshToken, jwtService);
    }

    @Bean
    public RevocarRefreshToken revocarRefreshTokenUseCase(RefreshTokenRepository refreshTokenRepository) {
        return new RevocarRefreshTokenUseCase(refreshTokenRepository);
    }
}