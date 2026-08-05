package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.domain.model.Usuario;

public interface ObtenerPerfilPropio {
    Usuario obtener(String username);
}
