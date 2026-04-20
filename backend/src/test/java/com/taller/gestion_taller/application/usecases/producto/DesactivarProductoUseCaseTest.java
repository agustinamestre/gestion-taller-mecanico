package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
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
@DisplayName("DesactivarProductoUseCase")
class DesactivarProductoUseCaseTest {

    private static final Long ID = 1L;

    @Mock private ProductoRepository productoRepository;

    @InjectMocks
    private DesactivarProductoUseCase useCase;

    @Test
    @DisplayName("debe desactivar un producto exitosamente")
    void debeDesactivarProductoExitosamente() {
        // Arrange
        Producto producto = mock(Producto.class);
        Producto productoDesactivado = mock(Producto.class);

        when(productoRepository.findById(ID)).thenReturn(Optional.of(producto));
        when(producto.desactivar()).thenReturn(productoDesactivado);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoDesactivado);

        // Act
        useCase.desactivar(ID);

        // Assert
        verify(productoRepository).findById(ID);
        verify(producto).desactivar();
        verify(productoRepository).save(productoDesactivado);
    }

    @Test
    @DisplayName("debe lanzar excepcion si el producto no existe")
    void debeLanzarExcepcionSiProductoNoExiste() {
        // Arrange
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.desactivar(ID))
                .isInstanceOf(NotFoundException.class);
    }
}
