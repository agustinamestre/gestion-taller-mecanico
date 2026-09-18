package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReactivarUsuarioUseCase implements ReactivarUsuario {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void reactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.usuarioNoEncontrado(id)));

        if (usuario.isActivo()) {
            throw new BusinessRunTimeException(
                    BusinessErrors.usuarioYaActivo(id));
        }

        Usuario reactivado = usuario.toBuilder()
                .activo(true)
                .build();

        usuarioRepository.save(reactivado);
    }
}
