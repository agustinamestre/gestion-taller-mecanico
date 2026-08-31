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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EliminarPresupuestosVencidosYRechazadosUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @InjectMocks
    private EliminarPresupuestosVencidosYRechazadosUseCase eliminarPresupuestosVencidosYRechazadosUseCase;

    private static Presupuesto presupuestoConId(Long id) {
        return Presupuesto.builder()
                .id(id)
                .vehiculo(null)
                .fechaVencimiento(LocalDate.now().minusDays(200))
                .items(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Debe eliminar cada presupuesto VENCIDO o RECHAZADO con mas de 180 dias desde su vencimiento")
    void debeEliminarLosPresupuestosEncontradosYRetornarElTotal() {
        Presupuesto vencido = presupuestoConId(1L);
        Presupuesto rechazado = presupuestoConId(2L);

        when(presupuestoRepository.findByEstadoAndFechaVencimientoBefore(eq(EstadoPresupuesto.VENCIDO), any(LocalDate.class)))
                .thenReturn(List.of(vencido));
        when(presupuestoRepository.findByEstadoAndFechaVencimientoBefore(eq(EstadoPresupuesto.RECHAZADO), any(LocalDate.class)))
                .thenReturn(List.of(rechazado));

        int cantidad = eliminarPresupuestosVencidosYRechazadosUseCase.eliminar();

        assertEquals(2, cantidad);
        verify(presupuestoRepository).deleteById(1L);
        verify(presupuestoRepository).deleteById(2L);
    }

    @Test
    @DisplayName("No debe eliminar nada cuando no hay presupuestos candidatos")
    void noDebeHacerNadaSiNoHayPresupuestosCandidatos() {
        when(presupuestoRepository.findByEstadoAndFechaVencimientoBefore(eq(EstadoPresupuesto.VENCIDO), any(LocalDate.class)))
                .thenReturn(List.of());
        when(presupuestoRepository.findByEstadoAndFechaVencimientoBefore(eq(EstadoPresupuesto.RECHAZADO), any(LocalDate.class)))
                .thenReturn(List.of());

        int cantidad = eliminarPresupuestosVencidosYRechazadosUseCase.eliminar();

        assertEquals(0, cantidad);
        verify(presupuestoRepository, never()).deleteById(any());
    }
}
