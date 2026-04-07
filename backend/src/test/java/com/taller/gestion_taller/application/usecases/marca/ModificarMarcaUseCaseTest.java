package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.application.command.ModificarMarcaCommand;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.service.MarcaValidator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModificarMarcaUseCase")
class ModificarMarcaUseCaseTest {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private MarcaValidator marcaValidator;

    @InjectMocks
    private ModificarMarcaUseCase useCase;

    @Test
    @DisplayName("Modificar marca exitosamente")
    void debeModificarMarcaExitosamente() {
        Long id = 1L;
        ModificarMarcaCommand command = new ModificarMarcaCommand("Ford");
        Marca marcaExistente = mock(Marca.class);
        Marca marcaActualizada = mock(Marca.class);

        when(marcaRepository.findById(id)).thenReturn(Optional.of(marcaExistente));
        when(marcaExistente.getNombre()).thenReturn("Fiat");
        when(marcaExistente.actualizarNombre("Ford")).thenReturn(marcaExistente);
        when(marcaRepository.save(marcaExistente)).thenReturn(marcaActualizada);

        Marca resultado = useCase.modificarMarca(id, command);

        assertThat(resultado).isEqualTo(marcaActualizada);
        verify(marcaRepository).findById(id);
        verify(marcaValidator).validarNombreUnico("Ford", "Fiat");
        verify(marcaExistente).actualizarNombre("Ford");
        verify(marcaRepository).save(marcaExistente);
    }

    @Test
    @DisplayName("Lanzar excepción cuando la marca a modificar no existe")
    void debeLanzarExcepcionCuandoMarcaNoExiste() {
        Long id = 1L;
        ModificarMarcaCommand command = new ModificarMarcaCommand("Ford");

        when(marcaRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.modificarMarca(id, command);
        });

        assertTrue(exception.getMessage().contains("No se encontro la marca ingresada."));
        verify(marcaRepository, never()).save(any());
    }
}
