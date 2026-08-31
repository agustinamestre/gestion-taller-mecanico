package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.RegistrarUsuarioCommand;
import com.taller.gestion_taller.application.mapper.UsuarioApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Rol;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarUsuarioUseCase")
class RegistrarUsuarioUseCaseTest {

    private static final String CLAVE_FIXTURE = "fixture-clave-registro";
    private static final String CLAVE_HASH_FIXTURE = "fixture-hash-registro";

    private static final RegistrarUsuarioCommand COMMAND = new RegistrarUsuarioCommand(
            "jperez", CLAVE_FIXTURE, "Juan", "Perez", Rol.EMPLEADO
    );

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioApplicationMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrarUsuarioUseCase useCase;

    @Test
    @DisplayName("Debe registrar el usuario con la contrasena encriptada")
    void debeRegistrarUsuarioExitosamente() {
        Usuario usuarioSinHash = Usuario.builder()
                .username("jperez")
                .nombre("Juan")
                .apellido("Perez")
                .rol(Rol.EMPLEADO)
                .activo(true)
                .build();

        Usuario usuarioGuardado = usuarioSinHash.toBuilder()
                .id(1L)
                .password(CLAVE_HASH_FIXTURE)
                .build();

        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.empty());
        when(mapper.commandToDomain(COMMAND)).thenReturn(usuarioSinHash);
        when(passwordEncoder.encode(CLAVE_FIXTURE)).thenReturn(CLAVE_HASH_FIXTURE);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        Usuario resultado = useCase.registrar(COMMAND);

        assertThat(resultado).isEqualTo(usuarioGuardado);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(CLAVE_HASH_FIXTURE);
        assertThat(captor.getValue().getUsername()).isEqualTo("jperez");
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el username ya existe")
    void debeLanzarExcepcionCuandoUsernameYaExiste() {
        Usuario usuarioExistente = Usuario.builder().id(5L).username("jperez").build();

        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuarioExistente));

        Exception exception = assertThrows(BusinessRunTimeException.class, () -> {
            useCase.registrar(COMMAND);
        });

        assertTrue(exception.getMessage().contains("Ya existe un usuario con el username: jperez"));
        verify(usuarioRepository, never()).save(any());
    }
}
