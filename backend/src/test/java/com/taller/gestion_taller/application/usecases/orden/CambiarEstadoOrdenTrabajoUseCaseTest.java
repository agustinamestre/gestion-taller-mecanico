package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CambiarEstadoOrdenTrabajoUseCase")
class CambiarEstadoOrdenTrabajoUseCaseTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @InjectMocks
    private CambiarEstadoOrdenTrabajoUseCase useCase;

    @Test
    @DisplayName("debe cambiar estado exitosamente cuando la transicion es valida")
    void debeCambiarEstadoExitosamente() {
        OrdenTrabajo orden = mock(OrdenTrabajo.class);
        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.EN_REPARACION));

        verify(orden).cambiarEstado(EstadoOrdenTrabajo.EN_REPARACION);
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe setear fechaEgreso al pasar a FINALIZADO")
    void debeSetearFechaEgresoAlFinalizar() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L)
                .estado(EstadoOrdenTrabajo.EN_REPARACION)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.FINALIZADO));

        assertThat(orden.getFechaEgreso()).isEqualTo(LocalDate.now());
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe lanzar excepcion si la orden no existe")
    void debeLanzarExcepcionSiOrdenNoExiste() {
        when(ordenTrabajoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(99L, EstadoOrdenTrabajo.EN_REPARACION)))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si la transicion es invalida")
    void debeLanzarExcepcionSiTransicionInvalida() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L)
                .estado(EstadoOrdenTrabajo.ENTREGADO)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        assertThatThrownBy(() ->
                useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.EN_REPARACION)))
                .isInstanceOf(BusinessRunTimeException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si CANCELADO intenta cambiar de estado")
    void debeLanzarExcepcionSiCanceladoIntentaCambiar() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L)
                .estado(EstadoOrdenTrabajo.CANCELADO)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        assertThatThrownBy(() ->
                useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.EN_REPARACION)))
                .isInstanceOf(BusinessRunTimeException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }
}