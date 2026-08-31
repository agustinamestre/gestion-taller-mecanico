package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.CambiarPasswordCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Rol;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@DisplayName("CambiarPasswordUseCase")
class CambiarPasswordUseCaseTest {

    private static final CambiarPasswordCommand COMMAND = new CambiarPasswordCommand(
            "jperez", "actual123", "nueva123"
    );

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CambiarPasswordUseCase useCase;

    private static Usuario usuarioExistente() {
        return Usuario.builder()
                .id(1L)
                .username("jperez")
                .password("hashActual")
                .nombre("Juan")
                .apellido("Perez")
                .rol(Rol.EMPLEADO)
                .activo(true)
                .build();
    }

    @Test
    @DisplayName("Debe cambiar la contraseña cuando la actual es correcta")
    void debeCambiarPasswordCuandoActualEsCorrecta() {
        Usuario usuario = usuarioExistente();

        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("actual123", "hashActual")).thenReturn(true);
        when(passwordEncoder.encode("nueva123")).thenReturn("hashNueva");

        useCase.cambiar(COMMAND);

        verify(usuarioRepository).save(argThat(u -> u.getPassword().equals("hashNueva")));
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el usuario no existe")
    void debeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.cambiar(COMMAND);
        });

        assertTrue(exception.getMessage().contains("No se encontró el usuario con username: jperez"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando la contraseña actual es incorrecta")
    void debeLanzarExcepcionCuandoPasswordActualEsIncorrecta() {
        Usuario usuario = usuarioExistente();

        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("actual123", "hashActual")).thenReturn(false);

        Exception exception = assertThrows(BusinessRunTimeException.class, () -> {
            useCase.cambiar(COMMAND);
        });

        assertTrue(exception.getMessage().contains("La contraseña actual ingresada es incorrecta."));
        verify(usuarioRepository, never()).save(any());
    }
}
