package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ActualizarStockProductoCommand;
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
class ActualizarStockProductoUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ActualizarStockProductoUseCase useCase;

    private Producto producto;
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(id)
                .nombre("Aceite")
                .tipo(TipoProducto.INSUMO)
                .stockActual(10)
                .precioActual(BigDecimal.valueOf(100))
                .activo(true)
                .build();
    }

    @Test
    void actualizar_debeActualizarElStockCorrectamente() {
        ActualizarStockProductoCommand command = ActualizarStockProductoCommand.builder()
                .nuevoStock(20)
                .build();

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = useCase.actualizar(id, command);

        assertNotNull(resultado);
        assertEquals(20, resultado.getStockActual());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizar_debeLanzarExcepcionSiProductoNoExiste() {
        ActualizarStockProductoCommand command = ActualizarStockProductoCommand.builder()
                .nuevoStock(20)
                .build();

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.actualizar(id, command));
        verify(productoRepository, never()).save(any());
    }
}
