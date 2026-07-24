package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarProductosUseCase")
class ListarProductosUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ListarProductosUseCase useCase;

    @Test
    @DisplayName("debe listar los productos exitosamente")
    void debeListarProductosExitosamente() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Filtro de Aceite")
                .descripcion("Filtro para motor")
                .tipo(TipoProducto.REPUESTO)
                .precioActual(new BigDecimal("1500.00"))
                .stockActual(10)
                .build();
        when(productoRepository.findAll()).thenReturn(Collections.singletonList(producto));

        List<Producto> resultado = useCase.listar();

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
    }
}
