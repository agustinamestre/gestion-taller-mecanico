package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DesactivarModeloUseCase")
class DesactivarModeloUseCaseTest {

    private static final Long ID = 1L;

    @Mock private ModeloRepository modeloRepository;

    @InjectMocks
    private DesactivarModeloUseCase useCase;

    @Test
    @DisplayName("debe desactivar un modelo exitosamente")
    void debeDesactivarModeloExitosamente() {
        // Arrange
        Modelo modelo = mock(Modelo.class);
        Modelo modeloDesactivado = mock(Modelo.class);
        
        when(modeloRepository.findById(ID)).thenReturn(Optional.of(modelo));
        when(modelo.desactivar()).thenReturn(modeloDesactivado);
        when(modeloRepository.save(any(Modelo.class))).thenReturn(modeloDesactivado);

        // Act
        useCase.desactivar(ID);

        // Assert
        verify(modeloRepository).findById(ID);
        verify(modelo).desactivar();
        verify(modeloRepository).save(modeloDesactivado);
    }

    @Test
    @DisplayName("debe lanzar excepcion si el modelo no existe")
    void debeLanzarExcepcionSiModeloNoExiste() {
        // Arrange
        when(modeloRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.desactivar(ID))
                .isInstanceOf(NotFoundException.class);
    }
}
