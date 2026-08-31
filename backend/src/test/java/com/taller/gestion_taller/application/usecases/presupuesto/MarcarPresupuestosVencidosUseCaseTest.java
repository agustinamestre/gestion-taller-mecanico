package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarcarPresupuestosVencidosUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @InjectMocks
    private MarcarPresupuestosVencidosUseCase marcarPresupuestosVencidosUseCase;

    private static Presupuesto presupuestoPendienteVencido(Long id) {
        return Presupuesto.builder()
                .id(id)
                .vehiculo(null)
                .estado(EstadoPresupuesto.PENDIENTE)
                .fechaVencimiento(LocalDate.now().minusDays(1))
                .items(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Debe marcar como VENCIDO cada presupuesto PENDIENTE con fecha de vencimiento pasada y persistirlo")
    void debeMarcarComoVencidoYPersistirCadaPresupuestoEncontrado() {
        Presupuesto primero = presupuestoPendienteVencido(1L);
        Presupuesto segundo = presupuestoPendienteVencido(2L);

        when(presupuestoRepository.findByEstadoAndFechaVencimientoBefore(eq(EstadoPresupuesto.PENDIENTE), any(LocalDate.class)))
                .thenReturn(List.of(primero, segundo));

        int cantidad = marcarPresupuestosVencidosUseCase.marcar();

        assertEquals(2, cantidad);
        assertEquals(EstadoPresupuesto.VENCIDO, primero.getEstado());
        assertEquals(EstadoPresupuesto.VENCIDO, segundo.getEstado());
        verify(presupuestoRepository, times(1)).save(primero);
        verify(presupuestoRepository, times(1)).save(segundo);
    }

    @Test
    @DisplayName("No debe hacer nada cuando no hay presupuestos pendientes vencidos")
    void noDebeHacerNadaSiNoHayPresupuestosVencidos() {
        when(presupuestoRepository.findByEstadoAndFechaVencimientoBefore(eq(EstadoPresupuesto.PENDIENTE), any(LocalDate.class)))
                .thenReturn(List.of());

        int cantidad = marcarPresupuestosVencidosUseCase.marcar();

        assertEquals(0, cantidad);
        verify(presupuestoRepository, never()).save(any());
    }
}
