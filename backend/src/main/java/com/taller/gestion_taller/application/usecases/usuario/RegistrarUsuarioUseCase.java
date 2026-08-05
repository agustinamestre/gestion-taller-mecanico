package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.RegistrarUsuarioCommand;
import com.taller.gestion_taller.application.mapper.UsuarioApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase implements RegistrarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioApplicationMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrar(RegistrarUsuarioCommand command) {

        usuarioRepository.findByUsername(command.username()).ifPresent(u -> {
            throw new BusinessRunTimeException(
                    BusinessErrors.usernameYaExiste(command.username()));
        });

        Usuario usuario = mapper.commandToDomain(command);

        Usuario usuarioConHash = usuario.toBuilder()
                .password(passwordEncoder.encode(command.password()))
                .build();

        return usuarioRepository.save(usuarioConHash);
    }
}
