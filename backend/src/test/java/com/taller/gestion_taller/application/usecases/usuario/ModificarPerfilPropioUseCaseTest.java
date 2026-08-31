package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.ModificarPerfilPropioCommand;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModificarPerfilPropioUseCase")
class ModificarPerfilPropioUseCaseTest {

    private static final ModificarPerfilPropioCommand COMMAND = new ModificarPerfilPropioCommand(
            "jperez", "Juan", "Perez Actualizado"
    );

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ModificarPerfilPropioUseCase useCase;

    @Test
    @DisplayName("Debe retornar el usuario con nombre y apellido actualizados")
    void debeRetornarUsuarioActualizado() {
        Usuario usuarioExistente = Usuario.builder()
                .id(1L)
                .username("jperez")
                .nombre("Juan")
                .apellido("Perez")
                .rol(Rol.EMPLEADO)
                .activo(true)
                .build();

        Usuario usuarioModificado = usuarioExistente.toBuilder()
                .apellido("Perez Actualizado")
                .build();

        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioModificado);

        Usuario resultado = useCase.modificar(COMMAND);

        assertThat(resultado).isEqualTo(usuarioModificado);
        assertThat(resultado.getApellido()).isEqualTo("Perez Actualizado");
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el usuario no existe")
    void debeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.modificar(COMMAND);
        });

        assertTrue(exception.getMessage().contains("No se encontró el usuario con username: jperez"));
        verify(usuarioRepository, never()).save(any());
    }
}
