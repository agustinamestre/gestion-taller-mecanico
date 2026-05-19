package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.AgregarItemOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgregarItemOrdenTrabajoUseCase")
class AgregarItemOrdenTrabajoUseCaseTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;
    @Mock
    private ProductoRepository productoRepository;
    @InjectMocks
    private AgregarItemOrdenTrabajoUseCase useCase;

    @Test
    @DisplayName("debe agregar item con producto exitosamente")
    void debeAgregarItemConProducto() {
        OrdenTrabajo orden = mock(OrdenTrabajo.class);
        Producto producto = mock(Producto.class);
        AgregarItemOrdenTrabajoCommand command = new AgregarItemOrdenTrabajoCommand(
                1L, 1L, "Cambio de aceite", 1, BigDecimal.valueOf(1500));

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ordenTrabajoRepository.save(orden)).thenReturn(orden);

        OrdenTrabajo resultado = useCase.agregar(command);

        assertThat(resultado).isEqualTo(orden);
        verify(orden).agregarItem(producto, "Cambio de aceite", 1, BigDecimal.valueOf(1500));
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe agregar item sin producto exitosamente")
    void debeAgregarItemSinProducto() {
        OrdenTrabajo orden = mock(OrdenTrabajo.class);
        AgregarItemOrdenTrabajoCommand command = new AgregarItemOrdenTrabajoCommand(
                1L, null, "Mano de obra", 1, BigDecimal.valueOf(2000));

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(ordenTrabajoRepository.save(orden)).thenReturn(orden);

        OrdenTrabajo resultado = useCase.agregar(command);

        assertThat(resultado).isEqualTo(orden);
        verify(productoRepository, never()).findById(any());
        verify(orden).agregarItem(null, "Mano de obra", 1, BigDecimal.valueOf(2000));
    }

    @Test
    @DisplayName("debe lanzar excepcion si la orden no existe")
    void debeLanzarExcepcionSiOrdenNoExiste() {
        when(ordenTrabajoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.agregar(
                new AgregarItemOrdenTrabajoCommand(99L, null, "desc", 1, BigDecimal.ONE)))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si el producto no existe")
    void debeLanzarExcepcionSiProductoNoExiste() {
        OrdenTrabajo orden = mock(OrdenTrabajo.class);
        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.agregar(
                new AgregarItemOrdenTrabajoCommand(1L, 99L, "desc", 1, BigDecimal.ONE)))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }
}
