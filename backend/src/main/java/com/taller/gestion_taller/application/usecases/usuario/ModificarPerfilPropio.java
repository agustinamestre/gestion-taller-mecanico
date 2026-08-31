package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.ModificarPerfilPropioCommand;
import com.taller.gestion_taller.domain.model.Usuario;

public interface ModificarPerfilPropio {
    Usuario modificar(ModificarPerfilPropioCommand command);
}
