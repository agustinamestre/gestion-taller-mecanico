package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.usuario.ModificarUsuarioCommand;
import com.taller.gestion_taller.application.command.usuario.RegistrarUsuarioCommand;
import com.taller.gestion_taller.application.usecases.usuario.*;
import com.taller.gestion_taller.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final RegistrarUsuario registrarUsuarioUseCase;
    private final ObtenerUsuarios obtenerUsuariosUseCase;
    private final ModificarUsuario modificarUsuarioUseCase;
    private final DesactivarUsuario desactivarUsuarioUseCase;
    private final ObtenerPerfilPropio obtenerPerfilPropioUseCase;

    @Transactional
    public Usuario registrar(RegistrarUsuarioCommand command) {
        return registrarUsuarioUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return obtenerUsuariosUseCase.obtenerTodos();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return obtenerUsuariosUseCase.obtenerPorId(id);
    }

    @Transactional
    public Usuario modificar(ModificarUsuarioCommand command) {
        return modificarUsuarioUseCase.modificar(command);
    }

    @Transactional
    public void desactivar(Long id) {
        desactivarUsuarioUseCase.desactivar(id);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPerfilPropio(String username) {
        return obtenerPerfilPropioUseCase.obtener(username);
    }
}
