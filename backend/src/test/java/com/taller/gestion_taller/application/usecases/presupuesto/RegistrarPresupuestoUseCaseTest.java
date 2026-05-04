package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.*;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarPresupuestoUseCaseTest {

    private static final int DIAS_VENCIMIENTO_DEFAULT = 30;

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private RegistrarPresupuestoUseCase registrarPresupuestoUseCase;

    private Vehiculo vehiculoDePrueba;

    @BeforeEach
    void setUp() {
        Cliente cliente = Cliente.builder().id(1L).activo(true).build();
        Marca marca = Marca.builder().id(1L).nombre("Ford").activo(true).build();
        Modelo modelo = Modelo.builder().id(1L).nombre("Focus").marca(marca).activo(true).build();

        vehiculoDePrueba = Vehiculo.builder()
                .id(1L)
                .patente("ABC-123")
                .modelo(modelo)
                .anio(2020)
                .cliente(cliente)
                .kilometrajeActual(15000)
                .activo(true)
                .build();
    }

    @Test
    void registrar_DebeCrearPresupuesto_ConVehiculoAsociado() {
        // Arrange
        RegistrarPresupuestoCommand command = new RegistrarPresupuestoCommand(
                1L, "Revisión completa del motor");

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculoDePrueba));
        when(presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(invocation -> {
            Presupuesto p = invocation.getArgument(0);
            return p.toBuilder().id(1L).build();
        });

        // Act
        Presupuesto result = registrarPresupuestoUseCase.registrar(command);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(vehiculoDePrueba, result.getVehiculo());
        assertEquals(EstadoPresupuesto.PENDIENTE, result.getEstado());
        assertEquals("Revisión completa del motor", result.getObservaciones());

        verify(vehiculoRepository).findById(1L);
        verify(presupuestoRepository).save(any(Presupuesto.class));
    }

    @Test
    void registrar_DebeCrearPresupuesto_SinVehiculoAsociado() {
        // Arrange
        RegistrarPresupuestoCommand command = new RegistrarPresupuestoCommand(
                null, "Presupuesto para cliente potencial");

        when(presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(invocation -> {
            Presupuesto p = invocation.getArgument(0);
            return p.toBuilder().id(2L).build();
        });

        // Act
        Presupuesto result = registrarPresupuestoUseCase.registrar(command);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertNull(result.getVehiculo());
        assertEquals(EstadoPresupuesto.PENDIENTE, result.getEstado());

        verify(vehiculoRepository, never()).findById(anyLong());
        verify(presupuestoRepository).save(any(Presupuesto.class));
    }

    @Test
    void registrar_DebeCrearPresupuesto_SinObservaciones() {
        // Arrange
        RegistrarPresupuestoCommand command = new RegistrarPresupuestoCommand(1L, null);

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculoDePrueba));
        when(presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(invocation -> {
            Presupuesto p = invocation.getArgument(0);
            return p.toBuilder().id(3L).build();
        });

        // Act
        Presupuesto result = registrarPresupuestoUseCase.registrar(command);

        // Assert
        assertNotNull(result);
        assertNull(result.getObservaciones());
    }

    @Test
    void registrar_DebeAsignarFechaEmisionHoy_YVencimientoA30Dias() {
        // Arrange
        RegistrarPresupuestoCommand command = new RegistrarPresupuestoCommand(null, null);
        LocalDate hoy = LocalDate.now();

        when(presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(invocation -> {
            Presupuesto p = invocation.getArgument(0);
            return p.toBuilder().id(4L).build();
        });

        // Act
        Presupuesto result = registrarPresupuestoUseCase.registrar(command);

        // Assert
        assertEquals(hoy, result.getFechaEmision());
        assertEquals(hoy.plusDays(DIAS_VENCIMIENTO_DEFAULT), result.getFechaVencimiento());
    }

    @Test
    void registrar_DebeLanzarExcepcion_CuandoVehiculoNoExiste() {
        // Arrange
        RegistrarPresupuestoCommand command = new RegistrarPresupuestoCommand(999L, "Obs");

        when(vehiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> registrarPresupuestoUseCase.registrar(command));

        assertEquals("VEHICULO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }
}
