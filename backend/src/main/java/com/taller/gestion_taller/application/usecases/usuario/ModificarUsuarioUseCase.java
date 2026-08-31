package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.ModificarUsuarioCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarUsuarioUseCase implements ModificarUsuario {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario modificar(ModificarUsuarioCommand command) {
        Usuario usuario = usuarioRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.usuarioNoEncontrado(command.id())));

        Usuario modificado = usuario.toBuilder()
                .nombre(command.nombre())
                .apellido(command.apellido())
                .telefono(command.telefono())
                .email(command.email())
                .direccion(command.direccion())
                .rol(command.rol())
                .build();

        return usuarioRepository.save(modificado);
    }
}
