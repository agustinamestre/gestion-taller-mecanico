package com.taller.gestion_taller.application.usecases.auth;

import com.taller.gestion_taller.application.command.auth.RefrescarTokenCommand;
import com.taller.gestion_taller.application.dto.TokensRenovados;

public interface RefrescarToken {
    TokensRenovados refrescar(RefrescarTokenCommand command);
}