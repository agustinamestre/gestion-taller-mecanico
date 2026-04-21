package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ActualizarPrecioProductoCommand;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarPrecioProductoUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ActualizarPrecioProductoUseCase useCase;

    private Producto producto;
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(id)
                .nombre("Aceite")
                .tipo(TipoProducto.INSUMO)
                .precioActual(BigDecimal.valueOf(100))
                .activo(true)
                .build();
    }

    @Test
    void actualizar_debeActualizarElPrecioCorrectamente() {
        ActualizarPrecioProductoCommand command = ActualizarPrecioProductoCommand.builder()
                .nuevoPrecio(BigDecimal.valueOf(150))
                .build();

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = useCase.actualizar(id, command);

        assertNotNull(resultado);
        assertEquals(BigDecimal.valueOf(150), resultado.getPrecioActual());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizar_debeLanzarExcepcionSiProductoNoExiste() {
        ActualizarPrecioProductoCommand command = ActualizarPrecioProductoCommand.builder()
                .nuevoPrecio(BigDecimal.valueOf(150))
                .build();

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.actualizar(id, command));
        verify(productoRepository, never()).save(any());
    }
}
