package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.usuario.CambiarPasswordCommand;
import com.taller.gestion_taller.application.command.usuario.ModificarPerfilPropioCommand;
import com.taller.gestion_taller.application.command.usuario.ModificarUsuarioCommand;
import com.taller.gestion_taller.application.command.usuario.RegistrarUsuarioCommand;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerUsuarioController;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.CambiarPasswordRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.ModificarPerfilPropioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.ModificarUsuarioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.UsuarioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.response.UsuarioResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.UsuarioRestMapper;
import com.taller.gestion_taller.infrastructure.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController implements SwaggerUsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRestMapper usuarioRestMapper;

    @Override
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        RegistrarUsuarioCommand command = usuarioRestMapper.requestToCommand(request);
        Usuario usuario = usuarioService.registrar(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioRestMapper.domainToResponse(usuario));
    }

    @Override
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        List<UsuarioResponse> response = usuarioService.obtenerTodos()
                .stream()
                .map(usuarioRestMapper::domainToResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                usuarioRestMapper.domainToResponse(usuarioService.obtenerPorId(id)));
    }

    @Override
    public ResponseEntity<UsuarioResponse> modificar(@PathVariable Long id,
                                                     @Valid @RequestBody ModificarUsuarioRequest request) {
        ModificarUsuarioCommand command = usuarioRestMapper.requestToModificarCommand(id, request);
        return ResponseEntity.ok(
                usuarioRestMapper.domainToResponse(usuarioService.modificar(command)));
    }

    @Override
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UsuarioResponse> obtenerPerfilPropio(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                usuarioRestMapper.domainToResponse(usuarioService.obtenerPerfilPropio(username)));
    }

    @Override
    public ResponseEntity<UsuarioResponse> modificarPerfilPropio(Authentication authentication,
                                                                  @Valid @RequestBody ModificarPerfilPropioRequest request) {
        ModificarPerfilPropioCommand command = usuarioRestMapper.requestToModificarPerfilCommand(authentication.getName(), request);
        return ResponseEntity.ok(
                usuarioRestMapper.domainToResponse(usuarioService.modificarPerfilPropio(command)));
    }

    @Override
    public ResponseEntity<Void> cambiarPassword(Authentication authentication,
                                                 @Valid @RequestBody CambiarPasswordRequest request) {
        CambiarPasswordCommand command = usuarioRestMapper.requestToCambiarPasswordCommand(authentication.getName(), request);
        usuarioService.cambiarPassword(command);
        return ResponseEntity.noContent().build();
    }
}
