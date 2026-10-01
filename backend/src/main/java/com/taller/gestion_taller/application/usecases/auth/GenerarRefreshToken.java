package com.taller.gestion_taller.application.usecases.auth;

public interface GenerarRefreshToken {
    String generar(Long usuarioId);
}