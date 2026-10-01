package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.auth.RefrescarTokenCommand;
import com.taller.gestion_taller.application.dto.TokensRenovados;
import com.taller.gestion_taller.application.usecases.auth.GenerarRefreshToken;
import com.taller.gestion_taller.application.usecases.auth.RefrescarToken;
import com.taller.gestion_taller.application.usecases.auth.RevocarRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GenerarRefreshToken generarRefreshToken;
    private final RefrescarToken refrescarToken;
    private final RevocarRefreshToken revocarRefreshToken;

    @Transactional
    public String generarRefreshToken(Long usuarioId) {
        return generarRefreshToken.generar(usuarioId);
    }

    @Transactional
    public TokensRenovados refrescarToken(RefrescarTokenCommand command) {
        return refrescarToken.refrescar(command);
    }

    @Transactional
    public void logout(Long usuarioId) {
        revocarRefreshToken.revocar(usuarioId);
    }
}