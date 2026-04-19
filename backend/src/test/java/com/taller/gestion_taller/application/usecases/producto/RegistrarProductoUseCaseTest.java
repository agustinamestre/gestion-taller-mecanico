package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.application.mapper.ProductoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarProductoUseCase")
class RegistrarProductoUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private ProductoApplicationMapper applicationMapper;

    @InjectMocks
    private RegistrarProductoUseCase useCase;

    @Test
    @DisplayName("debe registrar un producto exitosamente")
    void debeRegistrarProductoExitosamente() {
        RegistrarProductoCommand command = new RegistrarProductoCommand(
                "Filtro de Aceite", 
                "Filtro para motor", 
                "REPUESTO", 
                new BigDecimal("1500.00"), 
                10
        );

        Producto producto = Producto.builder()
                .nombre("Filtro de Aceite")
                .tipo(TipoProducto.REPUESTO)
                .activo(true)
                .build();

        when(productoRepository.existePorNombreYTipo("Filtro de Aceite", TipoProducto.REPUESTO)).thenReturn(false);
        when(applicationMapper.commandToDomain(command)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = useCase.registrar(command);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Filtro de Aceite");
        assertThat(resultado.getTipo()).isEqualTo(TipoProducto.REPUESTO);
        assertThat(resultado.isActivo()).isTrue();
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("debe lanzar excepcion si el producto ya existe")
    void debeLanzarExcepcionSiProductoYaExiste() {
        RegistrarProductoCommand command = new RegistrarProductoCommand(
                "Filtro de Aceite", 
                "Filtro para motor", 
                "REPUESTO", 
                new BigDecimal("1500.00"), 
                10
        );

        when(productoRepository.existePorNombreYTipo("Filtro de Aceite", TipoProducto.REPUESTO)).thenReturn(true);

        assertThatThrownBy(() -> useCase.registrar(command))
                .isInstanceOf(BusinessRunTimeException.class)
                .hasMessageContaining("Ya existe");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si el tipo de producto es inválido")
    void debeLanzarExcepcionSiTipoInvalido() {
        RegistrarProductoCommand command = new RegistrarProductoCommand(
                "Filtro", 
                "Desc", 
                "INVALIDO", 
                BigDecimal.ZERO, 
                0
        );

        assertThatThrownBy(() -> useCase.registrar(command))
                .isInstanceOf(BusinessRunTimeException.class)
                .hasMessageContaining("tipo");
    }
}
