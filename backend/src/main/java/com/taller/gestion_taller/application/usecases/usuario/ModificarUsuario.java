package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.ModificarUsuarioCommand;
import com.taller.gestion_taller.domain.model.Usuario;

public interface ModificarUsuario {
    Usuario modificar(ModificarUsuarioCommand command);
}
