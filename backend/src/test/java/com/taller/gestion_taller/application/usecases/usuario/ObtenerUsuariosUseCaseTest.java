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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObtenerUsuariosUseCase")
class ObtenerUsuariosUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ObtenerUsuariosUseCase useCase;

    @Test
    @DisplayName("Debe retornar todos los usuarios registrados")
    void debeRetornarTodosLosUsuarios() {
        Usuario usuario1 = Usuario.builder().id(1L).username("jperez").rol(Rol.EMPLEADO).build();
        Usuario usuario2 = Usuario.builder().id(2L).username("mgomez").rol(Rol.ADMIN).build();

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        List<Usuario> resultado = useCase.obtenerTodos();

        assertThat(resultado).containsExactly(usuario1, usuario2);
    }

    @Test
    @DisplayName("Debe retornar lista vacia cuando no hay usuarios")
    void debeRetornarListaVaciaSiNoHayUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> resultado = useCase.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar el usuario cuando existe por id")
    void debeRetornarUsuarioPorId() {
        Usuario usuario = Usuario.builder().id(1L).username("jperez").rol(Rol.EMPLEADO).build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = useCase.obtenerPorId(1L);

        assertThat(resultado).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el usuario por id no existe")
    void debeLanzarExcepcionCuandoUsuarioPorIdNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.obtenerPorId(1L);
        });

        assertTrue(exception.getMessage().contains("No se encontró el usuario con ID: 1"));
    }
}
