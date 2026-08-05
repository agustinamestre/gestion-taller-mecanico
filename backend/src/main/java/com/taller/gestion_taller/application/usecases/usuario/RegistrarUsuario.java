package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.RegistrarUsuarioCommand;
import com.taller.gestion_taller.domain.model.Usuario;

public interface RegistrarUsuario {
    Usuario registrar(RegistrarUsuarioCommand command);
}
