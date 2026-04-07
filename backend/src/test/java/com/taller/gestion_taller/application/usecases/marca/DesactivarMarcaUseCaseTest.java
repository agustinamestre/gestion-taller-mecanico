package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DesactivarMarcaUseCase")
class DesactivarMarcaUseCaseTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private DesactivarMarcaUseCase useCase;

    @Test
    @DisplayName("Desactivar marca exitosamente")
    void debeDesactivarMarcaExitosamente() {
        Long id = 1L;
        Marca marca = mock(Marca.class);

        when(marcaRepository.findById(id)).thenReturn(Optional.of(marca));
        when(marca.desactivar()).thenReturn(marca);
        when(marcaRepository.save(marca)).thenReturn(marca);

        useCase.desactivarMarca(id);

        verify(marcaRepository).findById(id);
        verify(marca).desactivar();
        verify(marcaRepository).save(marca);
    }

    @Test
    @DisplayName("Lanzar excepción cuando la marca a desactivar no existe")
    void debeLanzarExcepcionCuandoMarcaNoExiste() {
        Long id = 1L;

        when(marcaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.desactivarMarca(id));
        verify(marcaRepository, never()).save(any());
    }
}
