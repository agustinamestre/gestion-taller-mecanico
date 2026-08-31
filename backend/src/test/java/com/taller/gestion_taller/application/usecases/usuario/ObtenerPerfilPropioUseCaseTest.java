package com.taller.gestion_taller.application.usecases.usuario;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObtenerPerfilPropioUseCase")
class ObtenerPerfilPropioUseCaseTest {

    private static final String USERNAME = "jperez";

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ObtenerPerfilPropioUseCase useCase;

    @Test
    @DisplayName("Debe retornar el perfil del usuario autenticado")
    void debeRetornarPerfilDelUsuario() {
        Usuario usuario = Usuario.builder().id(1L).username(USERNAME).rol(Rol.EMPLEADO).build();

        when(usuarioRepository.findByUsername(USERNAME)).thenReturn(Optional.of(usuario));

        Usuario resultado = useCase.obtener(USERNAME);

        assertThat(resultado).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el usuario autenticado no existe")
    void debeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.obtener(USERNAME);
        });

        assertTrue(exception.getMessage().contains("No se encontró el usuario con username: " + USERNAME));
    }
}
