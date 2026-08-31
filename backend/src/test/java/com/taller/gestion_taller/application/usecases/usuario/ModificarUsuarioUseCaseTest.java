package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.ModificarUsuarioCommand;
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
@DisplayName("ModificarUsuarioUseCase")
class ModificarUsuarioUseCaseTest {

    private static final ModificarUsuarioCommand COMMAND = new ModificarUsuarioCommand(
            1L, "Juan", "Perez Actualizado", "1122334455", "jperez@taller.com", "Calle Falsa 123", Rol.ADMIN
    );

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ModificarUsuarioUseCase useCase;

    @Test
    @DisplayName("Debe retornar el usuario con los datos actualizados")
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
                .telefono("1122334455")
                .email("jperez@taller.com")
                .direccion("Calle Falsa 123")
                .rol(Rol.ADMIN)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioModificado);

        Usuario resultado = useCase.modificar(COMMAND);

        assertThat(resultado).isEqualTo(usuarioModificado);
        assertThat(resultado.getApellido()).isEqualTo("Perez Actualizado");
        assertThat(resultado.getRol()).isEqualTo(Rol.ADMIN);
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el usuario a modificar no existe")
    void debeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.modificar(COMMAND);
        });

        assertTrue(exception.getMessage().contains("No se encontró el usuario con ID: 1"));
        verify(usuarioRepository, never()).save(any());
    }
}
