package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DesactivarUsuarioUseCase")
class DesactivarUsuarioUseCaseTest {

    private static final Long ID = 1L;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DesactivarUsuarioUseCase useCase;

    @Test
    @DisplayName("Debe desactivar un usuario activo exitosamente")
    void debeDesactivarUsuarioExitosamente() {
        Usuario usuarioActivo = Usuario.builder().id(ID).username("jperez").activo(true).build();

        when(usuarioRepository.findById(ID)).thenReturn(Optional.of(usuarioActivo));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActivo.toBuilder().activo(false).build());

        useCase.desactivar(ID);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().isActivo()).isFalse();
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el usuario a desactivar no existe")
    void debeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.desactivar(ID);
        });

        assertTrue(exception.getMessage().contains("No se encontró el usuario con ID: 1"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el usuario ya esta desactivado")
    void debeLanzarExcepcionCuandoUsuarioYaEstaDesactivado() {
        Usuario usuarioInactivo = Usuario.builder().id(ID).username("jperez").activo(false).build();

        when(usuarioRepository.findById(ID)).thenReturn(Optional.of(usuarioInactivo));

        Exception exception = assertThrows(BusinessRunTimeException.class, () -> {
            useCase.desactivar(ID);
        });

        assertTrue(exception.getMessage().contains("El usuario con ID 1 ya se encuentra desactivado."));
        verify(usuarioRepository, never()).save(any());
    }
}
