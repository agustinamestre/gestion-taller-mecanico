package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ModificarProductoCommand;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import com.taller.gestion_taller.domain.service.ProductoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModificarProductoUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoValidator productoValidator;

    @InjectMocks
    private ModificarProductoUseCase modificarProductoUseCase;

    private Producto productoExistente;

    @BeforeEach
    void setUp() {
        productoExistente = Producto.builder()
                .id(1L)
                .nombre("Aceite")
                .descripcion("Aceite de motor")
                .tipo(TipoProducto.INSUMO)
                .build();
    }

    @Test
    @DisplayName("debe retornar producto modificado cuando existe")
    void debeRetornarProductoModificadoCuandoExiste() {
        ModificarProductoCommand command = new ModificarProductoCommand(
                "Nuevo Nombre", "Nueva descripción", "REPUESTO");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto resultado = modificarProductoUseCase.modificar(1L, command);

        assertThat(resultado.getNombre()).isEqualTo("Nuevo Nombre");
        assertThat(resultado.getDescripcion()).isEqualTo("Nueva descripción");
        assertThat(resultado.getTipo()).isEqualTo(TipoProducto.REPUESTO);
        assertThat(resultado.isActivo()).isTrue();

        verify(productoValidator).validarUnicidad(
                "Nuevo Nombre", TipoProducto.REPUESTO,
                "Aceite", TipoProducto.INSUMO
        );
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el producto no existe")
    void debeLanzarNotFoundExceptionCuandoProductoNoExiste() {
        ModificarProductoCommand command = new ModificarProductoCommand("Nombre", "Descripción", "REPUESTO");
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> modificarProductoUseCase.modificar(1L, command));
        verify(productoRepository, never()).save(any(Producto.class));
    }
}
