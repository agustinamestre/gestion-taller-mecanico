package com.taller.gestion_taller.application.usecases.auth;

public interface RevocarRefreshToken {
    void revocar(Long usuarioId);
}