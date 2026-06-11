package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.CambiarEstadoPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CambiarEstadoPresupuestoUseCaseTest {

    private static final Long PRESUPUESTO_ID = 1L;

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @InjectMocks
    private CambiarEstadoPresupuestoUseCase cambiarEstadoPresupuestoUseCase;

    @Test
    @DisplayName("Debe cambiar el estado a APROBADO cuando la transicion es valida y hay vehiculo")
    void debeCambiarElEstadoCuandoLaTransicionEsValida() {
        Presupuesto presupuesto = Presupuesto.builder()
                .id(PRESUPUESTO_ID)
                .vehiculo(mock(Vehiculo.class))
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));

        cambiarEstadoPresupuestoUseCase.cambiar(
                new CambiarEstadoPresupuestoCommand(PRESUPUESTO_ID, EstadoPresupuesto.APROBADO));

        assertEquals(EstadoPresupuesto.APROBADO, presupuesto.getEstado());
        verify(presupuestoRepository).save(presupuesto);
    }

    @Test
    @DisplayName("Debe permitir RECHAZAR aunque el presupuesto no tenga vehiculo")
    void debePermitirRechazarSinVehiculo() {
        Presupuesto presupuesto = Presupuesto.builder()
                .id(PRESUPUESTO_ID)
                .vehiculo(null)
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));

        cambiarEstadoPresupuestoUseCase.cambiar(
                new CambiarEstadoPresupuestoCommand(PRESUPUESTO_ID, EstadoPresupuesto.RECHAZADO));

        assertEquals(EstadoPresupuesto.RECHAZADO, presupuesto.getEstado());
        verify(presupuestoRepository).save(presupuesto);
    }

    @Test
    @DisplayName("R2: NO debe permitir aprobar un presupuesto sin vehiculo asociado")
    void noDebePermitirAprobarSinVehiculo() {
        Presupuesto presupuesto = Presupuesto.builder()
                .id(PRESUPUESTO_ID)
                .vehiculo(null)
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () ->
                cambiarEstadoPresupuestoUseCase.cambiar(
                        new CambiarEstadoPresupuestoCommand(PRESUPUESTO_ID, EstadoPresupuesto.APROBADO)));

        assertEquals("PRESUPUESTO_SIN_VEHICULO", exception.getBusinessError().code());
        assertEquals(EstadoPresupuesto.PENDIENTE, presupuesto.getEstado());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar NotFoundException cuando el presupuesto no existe")
    void debeRetornarNotFoundExceptionCuandoElPresupuestoNoExiste() {
        Long presupuestoIdInexistente = 99L;

        when(presupuestoRepository.findById(presupuestoIdInexistente)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                cambiarEstadoPresupuestoUseCase.cambiar(
                        new CambiarEstadoPresupuestoCommand(presupuestoIdInexistente, EstadoPresupuesto.APROBADO)));

        assertEquals("PRESUPUESTO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar BusinessRunTimeException cuando la transicion es invalida")
    void debeRetornarBusinessRunTimeExceptionCuandoLaTransicionEsInvalida() {
        Presupuesto presupuesto = Presupuesto.builder()
                .id(PRESUPUESTO_ID)
                .vehiculo(mock(Vehiculo.class))
                .estado(EstadoPresupuesto.APROBADO)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () ->
                cambiarEstadoPresupuestoUseCase.cambiar(
                        new CambiarEstadoPresupuestoCommand(PRESUPUESTO_ID, EstadoPresupuesto.RECHAZADO)));

        assertEquals("TRANSICION_ESTADO_INVALIDA", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }
}
