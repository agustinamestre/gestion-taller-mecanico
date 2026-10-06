package com.taller.gestion_taller.application.usecases.auth;

import com.taller.gestion_taller.application.command.auth.RefrescarTokenCommand;
import com.taller.gestion_taller.application.dto.TokensRenovados;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.UnauthorizedException;
import com.taller.gestion_taller.domain.model.RefreshToken;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.RefreshTokenRepository;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import com.taller.gestion_taller.infrastructure.security.jwt.JwtService;
import com.taller.gestion_taller.infrastructure.security.refresh.RefreshTokenHasher;
import com.taller.gestion_taller.infrastructure.security.userdetails.UsuarioDetails;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefrescarTokenUseCase implements RefrescarToken {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenHasher refreshTokenHasher;
    private final GenerarRefreshToken generarRefreshToken;
    private final JwtService jwtService;

    @Override
    public TokensRenovados refrescar(RefrescarTokenCommand command) {
        String tokenHash = refreshTokenHasher.hashear(command.refreshToken());

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new UnauthorizedException(BusinessErrors.refreshTokenInvalido()));

        if (!refreshToken.esValido()) {
            throw new UnauthorizedException(BusinessErrors.refreshTokenInvalido());
        }

        // Rotation: el token usado queda invalidado, se emite uno nuevo
        refreshToken.revocar();
        refreshTokenRepository.save(refreshToken);

        Usuario usuario = usuarioRepository.findById(refreshToken.getUsuarioId())
                .orElseThrow(() -> new UnauthorizedException(BusinessErrors.refreshTokenInvalido()));

        if (!usuario.isActivo()) {
            throw new UnauthorizedException(BusinessErrors.refreshTokenInvalido());
        }

        String nuevoAccessToken = jwtService.generarToken(new UsuarioDetails(usuario));
        String nuevoRefreshToken = generarRefreshToken.generar(usuario.getId());

        return new TokensRenovados(nuevoAccessToken, nuevoRefreshToken);
    }
}