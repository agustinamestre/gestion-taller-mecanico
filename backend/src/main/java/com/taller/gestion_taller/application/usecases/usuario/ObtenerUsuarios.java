package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.domain.model.Usuario;

import java.util.List;

public interface ObtenerUsuarios {
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(Long id);
}
