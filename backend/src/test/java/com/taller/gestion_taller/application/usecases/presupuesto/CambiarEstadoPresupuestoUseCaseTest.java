package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.CambiarEstadoPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CambiarEstadoPresupuestoUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @InjectMocks
    private CambiarEstadoPresupuestoUseCase cambiarEstadoPresupuestoUseCase;

    @Test
    @DisplayName("Debe cambiar el estado cuando la transicion es valida")
    public void debeCambiarElEstadoCuandoLaTransicionEsValida() {
        Long presupuestoId = 1L;
        Presupuesto presupuesto = Presupuesto.builder()
                .id(presupuestoId)
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.of(presupuesto));

        cambiarEstadoPresupuestoUseCase.cambiar(
                new CambiarEstadoPresupuestoCommand(presupuestoId, EstadoPresupuesto.APROBADO));

        assertEquals(EstadoPresupuesto.APROBADO, presupuesto.getEstado());
        verify(presupuestoRepository).save(presupuesto);
    }

    @Test
    @DisplayName("Debe retornar NotFoundException cuando el presupuesto no existe")
    public void debeRetornarNotFoundExceptionCuandoElPresupuestoNoExiste() {
        Long presupuestoId = 99L;

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                cambiarEstadoPresupuestoUseCase.cambiar(
                        new CambiarEstadoPresupuestoCommand(presupuestoId, EstadoPresupuesto.APROBADO)));

        assertEquals("PRESUPUESTO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar BusinessRunTimeException cuando la transicion es invalida")
    public void debeRetornarBusinessRunTimeExceptionCuandoLaTransicionEsInvalida() {
        Long presupuestoId = 1L;
        Presupuesto presupuesto = Presupuesto.builder()
                .id(presupuestoId)
                .estado(EstadoPresupuesto.APROBADO)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.of(presupuesto));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () ->
                cambiarEstadoPresupuestoUseCase.cambiar(
                        new CambiarEstadoPresupuestoCommand(presupuestoId, EstadoPresupuesto.RECHAZADO)));

        assertEquals("TRANSICION_ESTADO_INVALIDA", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }
}