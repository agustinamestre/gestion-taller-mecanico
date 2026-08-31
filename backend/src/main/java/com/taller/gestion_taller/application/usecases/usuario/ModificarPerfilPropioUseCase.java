package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.ModificarPerfilPropioCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarPerfilPropioUseCase implements ModificarPerfilPropio {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario modificar(ModificarPerfilPropioCommand command) {
        Usuario usuario = usuarioRepository.findByUsername(command.username())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.usuarioNoEncontrado(command.username())));

        Usuario modificado = usuario.toBuilder()
                .nombre(command.nombre())
                .apellido(command.apellido())
                .telefono(command.telefono())
                .email(command.email())
                .direccion(command.direccion())
                .build();

        return usuarioRepository.save(modificado);
    }
}
