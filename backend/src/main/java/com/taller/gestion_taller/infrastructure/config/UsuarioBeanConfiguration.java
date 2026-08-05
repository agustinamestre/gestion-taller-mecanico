package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.UsuarioApplicationMapper;
import com.taller.gestion_taller.application.usecases.usuario.*;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioBeanConfiguration {

    @Bean
    public RegistrarUsuario registrarUsuarioUseCase(UsuarioRepository usuarioRepository,
                                                    UsuarioApplicationMapper mapper,
                                                    PasswordEncoder passwordEncoder) {
        return new RegistrarUsuarioUseCase(usuarioRepository, mapper, passwordEncoder);
    }

    @Bean
    public ObtenerUsuarios obtenerUsuariosUseCase(UsuarioRepository usuarioRepository) {
        return new ObtenerUsuariosUseCase(usuarioRepository);
    }

    @Bean
    public ModificarUsuario modificarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new ModificarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public DesactivarUsuario desactivarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new DesactivarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public ObtenerPerfilPropio obtenerPerfilPropioUseCase(UsuarioRepository usuarioRepository) {
        return new ObtenerPerfilPropioUseCase(usuarioRepository);
    }
}
