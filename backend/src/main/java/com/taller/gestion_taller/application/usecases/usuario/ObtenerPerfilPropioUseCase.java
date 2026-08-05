package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObtenerPerfilPropioUseCase implements ObtenerPerfilPropio {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario obtener(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.usuarioNoEncontrado(username)));
    }
}