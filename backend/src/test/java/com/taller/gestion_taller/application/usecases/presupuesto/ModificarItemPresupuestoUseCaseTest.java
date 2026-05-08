package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.ModificarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModificarItemPresupuestoUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ModificarItemPresupuestoUseCase useCase;

    private static final Long PRESUPUESTO_ID = 1L;
    private static final Long ITEM_ID = 10L;
    private static final Long PRODUCTO_ID = 1L;
    private static final Long PRODUCTO_NUEVO_ID = 2L;

    private Presupuesto presupuestoPendiente;
    private Producto productoOriginal;
    private Producto productoNuevo;

    @BeforeEach
    void setUp() {
        productoOriginal = Producto.builder()
                .id(PRODUCTO_ID)
                .nombre("Aceite 5W-30")
                .precioActual(new BigDecimal("25.00"))
                .build();

        productoNuevo = Producto.builder()
                .id(PRODUCTO_NUEVO_ID)
                .nombre("Filtro de aceite")
                .precioActual(new BigDecimal("15.00"))
                .build();

        ItemPresupuesto itemExistente = ItemPresupuesto.builder()
                .id(ITEM_ID)
                .presupuestoId(PRESUPUESTO_ID)
                .producto(productoOriginal)
                .descripcion("Cambio de aceite")
                .cantidad(2)
                .precioUnitario(new BigDecimal("25.00"))
                .build();

        presupuestoPendiente = Presupuesto.builder()
                .id(PRESUPUESTO_ID)
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>(List.of(itemExistente)))
                .build();
    }

    @Test
    @DisplayName("debe modificar los campos del item correctamente")
    void debeModificarLosCamposDelItemCorrectamente() {
        ModificarItemPresupuestoCommand command = comandoSinCambioDeProducto(
                "Descripción actualizada", 4, new BigDecimal("30.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));
        when(presupuestoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Presupuesto resultado = useCase.modificar(command);

        ItemPresupuesto itemModificado = resultado.getItems().get(0);
        assertAll("campos del item modificados correctamente",
                () -> assertEquals("Descripción actualizada", itemModificado.getDescripcion()),
                () -> assertEquals(4, itemModificado.getCantidad()),
                () -> assertEquals(new BigDecimal("30.00"), itemModificado.getPrecioUnitario())
        );
    }

    @Test
    @DisplayName("debe reemplazar el producto cuando se indica uno nuevo")
    void debeReemplazarElProductoCuandoSeIndicaUnoNuevo() {
        ModificarItemPresupuestoCommand command = comandoConProductoNuevo();

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));
        when(productoRepository.findById(PRODUCTO_NUEVO_ID)).thenReturn(Optional.of(productoNuevo));
        when(presupuestoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Presupuesto resultado = useCase.modificar(command);

        assertEquals(productoNuevo, resultado.getItems().getFirst().getProducto());
    }

    @Test
    @DisplayName("debe mantener el producto original cuando no se indica uno nuevo")
    void debeMantenerElProductoOriginalCuandoNoSeIndicaUnoNuevo() {
        ModificarItemPresupuestoCommand command = comandoSinCambioDeProducto(
                "Descripción", 1, new BigDecimal("25.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));
        when(presupuestoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Presupuesto resultado = useCase.modificar(command);

        assertEquals(productoOriginal, resultado.getItems().getFirst().getProducto());
        verifyNoInteractions(productoRepository);
    }

    @Test
    @DisplayName("debe recalcular el total correctamente después de modificar")
    void debeRecalcularElTotalCorrectamente() {
        ModificarItemPresupuestoCommand command = comandoSinCambioDeProducto(
                "Descripción", 3, new BigDecimal("100.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));
        when(presupuestoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Presupuesto resultado = useCase.modificar(command);

        assertEquals(0, new BigDecimal("300.00").compareTo(resultado.calcularTotal()));
    }

    @Test
    @DisplayName("debe persistir el presupuesto con el item actualizado")
    void debePersistirElPresupuestoConElItemActualizado() {
        ModificarItemPresupuestoCommand command = comandoSinCambioDeProducto(
                "Nueva descripción", 5, new BigDecimal("10.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));
        when(presupuestoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.modificar(command);

        verify(presupuestoRepository).save(argThat(p ->
                p.getItems().getFirst().getDescripcion().equals("Nueva descripción")
        ));
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el presupuesto no existe")
    void debeLanzarNotFoundExceptionCuandoPresupuestoNoExiste() {
        ModificarItemPresupuestoCommand command = new ModificarItemPresupuestoCommand(
                999L, ITEM_ID, null, "Desc", 1, new BigDecimal("10.00"));

        when(presupuestoRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.modificar(command));

        assertEquals("PRESUPUESTO_NO_ENCONTRADO", ex.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el item no existe en el presupuesto")
    void debeLanzarNotFoundExceptionCuandoItemNoExiste() {
        ModificarItemPresupuestoCommand command = new ModificarItemPresupuestoCommand(
                PRESUPUESTO_ID, 999L, null, "Desc", 1, new BigDecimal("10.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.modificar(command));

        assertEquals("ITEM_NO_ENCONTRADO", ex.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el producto nuevo no existe")
    void debeLanzarNotFoundExceptionCuandoProductoNuevoNoExiste() {
        ModificarItemPresupuestoCommand command = new ModificarItemPresupuestoCommand(
                PRESUPUESTO_ID, ITEM_ID, 999L, "Desc", 1, new BigDecimal("10.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.modificar(command));

        assertEquals("PRODUCTO_NO_ENCONTRADO", ex.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar BusinessException cuando el presupuesto no está pendiente")
    void debeLanzarBusinessExceptionCuandoPresupuestoNoEstaPendiente() {
        Presupuesto presupuestoAprobado = presupuestoPendiente.toBuilder()
                .estado(EstadoPresupuesto.APROBADO)
                .build();
        ModificarItemPresupuestoCommand command = comandoSinCambioDeProducto(
                "Desc", 1, new BigDecimal("10.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoAprobado));

        BusinessRunTimeException ex = assertThrows(BusinessRunTimeException.class,
                () -> useCase.modificar(command));

        assertEquals("PRESUPUESTO_NO_PENDIENTE", ex.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("no debe consultar el repositorio de productos cuando no cambia el producto")
    void noDebeConsultarProductoRepositoryCuandoNoSeCambiaElProducto() {
        ModificarItemPresupuestoCommand command = comandoSinCambioDeProducto(
                "Desc", 1, new BigDecimal("10.00"));

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuestoPendiente));
        when(presupuestoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.modificar(command);

        verifyNoInteractions(productoRepository);
    }

    // ─── Builders de comandos ─────────────────────────────────────────────────

    private ModificarItemPresupuestoCommand comandoSinCambioDeProducto(
            String descripcion, Integer cantidad, BigDecimal precio) {
        return new ModificarItemPresupuestoCommand(
                PRESUPUESTO_ID, ITEM_ID, null, descripcion, cantidad, precio);
    }

    private ModificarItemPresupuestoCommand comandoConProductoNuevo() {
        return new ModificarItemPresupuestoCommand(
                PRESUPUESTO_ID, ITEM_ID, PRODUCTO_NUEVO_ID, "Descripción", 2, new BigDecimal("15.00"));
    }
}
