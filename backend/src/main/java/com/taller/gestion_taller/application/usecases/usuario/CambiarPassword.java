package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.CambiarPasswordCommand;

public interface CambiarPassword {
    void cambiar(CambiarPasswordCommand command);
}
