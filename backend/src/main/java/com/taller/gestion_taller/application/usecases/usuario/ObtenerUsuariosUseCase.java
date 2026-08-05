package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ObtenerUsuariosUseCase implements ObtenerUsuarios {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.usuarioNoEncontrado(id)));
    }
}
