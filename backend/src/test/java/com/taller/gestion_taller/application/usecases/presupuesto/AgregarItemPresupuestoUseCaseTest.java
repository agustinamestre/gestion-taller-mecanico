package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgregarItemPresupuestoUseCase Tests")
class AgregarItemPresupuestoUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private AgregarItemPresupuestoUseCase agregarItemPresupuestoUseCase;

    private Presupuesto presupuestoExistente;
    private Producto productoRepuesto;
    private Producto productoServicio;

    @BeforeEach
    void setUp() {
        presupuestoExistente = Presupuesto.builder()
                .id(1L)
                .fechaEmision(LocalDate.now())
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>())
                .build();

        productoRepuesto = Producto.builder()
                .id(10L)
                .nombre("Filtro de aceite")
                .precioActual(new BigDecimal("15.00"))
                .stockActual(50)
                .build();

        productoServicio = Producto.builder()
                .id(20L)
                .nombre("Mano de obra - Cambio de aceite")
                .precioActual(new BigDecimal("50.00"))
                .stockActual(1000)
                .build();
    }

    @Test
    @DisplayName("Debe agregar un ítem de tipo REPUESTO a un presupuesto pendiente")
    void agregarRepuestoExitosamente() {
        // Arrange
        AgregarItemPresupuestoCommand command = new AgregarItemPresupuestoCommand(
                presupuestoExistente.getId(), "REPUESTO", productoRepuesto.getId(),
                "Filtro de aceite para motor", 1, new BigDecimal("15.00"));

        when(presupuestoRepository.findById(presupuestoExistente.getId())).thenReturn(Optional.of(presupuestoExistente));
        when(productoRepository.findById(productoRepuesto.getId())).thenReturn(Optional.of(productoRepuesto));
        when(presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Presupuesto presupuestoActualizado = agregarItemPresupuestoUseCase.agregar(command);

        // Assert
        assertThat(presupuestoActualizado.getItems()).hasSize(1);
        assertThat(presupuestoActualizado.getItems().get(0).getProducto()).isEqualTo(productoRepuesto);
        assertThat(presupuestoActualizado.getItems().get(0).getTipo()).isEqualTo(TipoProducto.REPUESTO);
        assertThat(presupuestoActualizado.calcularTotal()).isEqualByComparingTo("15.00");

        verify(presupuestoRepository).save(presupuestoActualizado);
    }

    @Test
    @DisplayName("Debe agregar múltiples ítems y calcular el total correctamente")
    void agregarMultiplesItems() {
        // Arrange
        AgregarItemPresupuestoCommand comandoRepuesto = new AgregarItemPresupuestoCommand(
                presupuestoExistente.getId(), "REPUESTO", productoRepuesto.getId(),
                "Filtro", 2, new BigDecimal("15.00")); // 2 * 15 = 30

        AgregarItemPresupuestoCommand comandoServicio = new AgregarItemPresupuestoCommand(
                presupuestoExistente.getId(), "SERVICIO", productoServicio.getId(),
                "Mano de obra", 1, new BigDecimal("50.00")); // 1 * 50 = 50

        when(presupuestoRepository.findById(presupuestoExistente.getId())).thenReturn(Optional.of(presupuestoExistente));
        when(productoRepository.findById(productoRepuesto.getId())).thenReturn(Optional.of(productoRepuesto));
        when(productoRepository.findById(productoServicio.getId())).thenReturn(Optional.of(productoServicio));
        when(presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        agregarItemPresupuestoUseCase.agregar(comandoRepuesto);
        Presupuesto presupuestoFinal = agregarItemPresupuestoUseCase.agregar(comandoServicio);

        // Assert
        assertThat(presupuestoFinal.getItems()).hasSize(2);
        assertThat(presupuestoFinal.calcularTotal()).isEqualByComparingTo("80.00"); // 30 + 50
    }


    @Test
    @DisplayName("Debe lanzar NotFoundException si el presupuesto no existe")
    void lanzarErrorSiPresupuestoNoExiste() {
        // Arrange
        AgregarItemPresupuestoCommand command = new AgregarItemPresupuestoCommand(
                999L, "REPUESTO", productoRepuesto.getId(), "Item", 1, BigDecimal.TEN);
        when(presupuestoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> agregarItemPresupuestoUseCase.agregar(command));

        assertEquals("PRESUPUESTO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException si el producto no existe")
    void lanzarErrorSiProductoNoExiste() {
        // Arrange
        AgregarItemPresupuestoCommand command = new AgregarItemPresupuestoCommand(
                presupuestoExistente.getId(), "REPUESTO", 999L, "Item", 1, BigDecimal.TEN);
        when(presupuestoRepository.findById(presupuestoExistente.getId())).thenReturn(Optional.of(presupuestoExistente));
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> agregarItemPresupuestoUseCase.agregar(command));

        assertEquals("PRODUCTO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar BusinessRunTimeException si el presupuesto no está PENDIENTE")
    void lanzarErrorSiPresupuestoNoEstaPendiente() {
        // Arrange
        presupuestoExistente = presupuestoExistente.toBuilder().estado(EstadoPresupuesto.APROBADO).build();
        AgregarItemPresupuestoCommand command = new AgregarItemPresupuestoCommand(
                presupuestoExistente.getId(), "REPUESTO", productoRepuesto.getId(), "Item", 1, BigDecimal.TEN);

        when(presupuestoRepository.findById(presupuestoExistente.getId())).thenReturn(Optional.of(presupuestoExistente));
        when(productoRepository.findById(productoRepuesto.getId())).thenReturn(Optional.of(productoRepuesto));

        // Act & Assert
        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class,
                () -> agregarItemPresupuestoUseCase.agregar(command));

        assertEquals("PRESUPUESTO_NO_PENDIENTE", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si el tipo de ítem es inválido")
    void lanzarErrorSiTipoEsInvalido() {
        // Arrange
        AgregarItemPresupuestoCommand command = new AgregarItemPresupuestoCommand(
                presupuestoExistente.getId(), "TIPO_INEXISTENTE", productoRepuesto.getId(), "Item", 1, BigDecimal.TEN);

        when(presupuestoRepository.findById(presupuestoExistente.getId())).thenReturn(Optional.of(presupuestoExistente));

        // Act & Assert
        // La excepción es lanzada por el enum TipoProducto.from()
        assertThrows(BusinessRunTimeException.class,
                () -> agregarItemPresupuestoUseCase.agregar(command));

        verify(presupuestoRepository, never()).save(any());
    }

}
