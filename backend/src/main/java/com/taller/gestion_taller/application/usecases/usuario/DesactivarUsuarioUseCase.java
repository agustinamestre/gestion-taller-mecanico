package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesactivarUsuarioUseCase implements DesactivarUsuario {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void desactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.usuarioNoEncontrado(id)));

        if (!usuario.isActivo()) {
            throw new BusinessRunTimeException(
                    BusinessErrors.usuarioYaDesactivado(id));
        }

        Usuario desactivado = usuario.toBuilder()
                .activo(false)
                .build();

        usuarioRepository.save(desactivado);
    }
}
