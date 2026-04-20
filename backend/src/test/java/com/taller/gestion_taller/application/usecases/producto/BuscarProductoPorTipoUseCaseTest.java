package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarProductoPorTipoUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private BuscarProductoPorTipoUseCase buscarProductoPorTipoUseCase;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Aceite 10W40")
                .descripcion("Aceite semisintético")
                .tipo(TipoProducto.INSUMO)
                .precioActual(new BigDecimal("15000"))
                .stockActual(10)
                .build();
    }

    @Test
    @DisplayName("debe retornar lista de productos cuando el tipo es válido")
    void debeRetornarListaCuandoTipoEsValido() {
        String tipoBusqueda = "INSUMO";
        when(productoRepository.buscarPorTipo(TipoProducto.INSUMO)).thenReturn(List.of(producto));

        List<Producto> resultado = buscarProductoPorTipoUseCase.buscarPorTipo(tipoBusqueda);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo(TipoProducto.INSUMO);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Aceite 10W40");
        verify(productoRepository).buscarPorTipo(TipoProducto.INSUMO); //
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando no hay productos del tipo")
    void debeLanzarNotFoundExceptionCuandoNoHayProductos() {
        String tipoBusqueda = "SERVICIO";
        when(productoRepository.buscarPorTipo(TipoProducto.SERVICIO)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> buscarProductoPorTipoUseCase.buscarPorTipo(tipoBusqueda));
    }

    @Test
    @DisplayName("debe lanzar excepcion cuando el tipo es inválido")
    void debeLanzarExcepcionCuandoTipoEsInvalido() {
        assertThatThrownBy(() -> buscarProductoPorTipoUseCase.buscarPorTipo("INVALIDO"))
                .isInstanceOf(BusinessRunTimeException.class)
                .hasMessageContaining("tipo");

        verifyNoInteractions(productoRepository);
    }
}
