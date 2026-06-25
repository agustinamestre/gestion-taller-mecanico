package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findAll();
}
