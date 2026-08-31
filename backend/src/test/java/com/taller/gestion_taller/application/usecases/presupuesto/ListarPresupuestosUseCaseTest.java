package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarPresupuestosUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @InjectMocks
    private ListarPresupuestosUseCase useCase;

    @Test
    @DisplayName("debe filtrar por patente y rango de fechas cuando ambos estan presentes")
    public void debeFiltrarPorPatenteYFechasCuandoAmbosEstanPresentes() {
        String patente = "ABC123";
        LocalDate desde = LocalDate.of(2026, 1, 1);
        LocalDate hasta = LocalDate.of(2026, 1, 31);
        Vehiculo vehiculo = Vehiculo.builder().patente(patente).build();
        List<Presupuesto> presupuestos = List.of(
                Presupuesto.builder().id(1L).vehiculo(vehiculo).build(),
                Presupuesto.builder().id(2L).vehiculo(vehiculo).build()
        );

        when(presupuestoRepository.findByPatenteAndFechaEmisionBetween(patente, desde, hasta))
                .thenReturn(presupuestos);

        List<Presupuesto> result = useCase.listar(patente, desde, hasta);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(presupuestoRepository).findByPatenteAndFechaEmisionBetween(patente, desde, hasta);
    }

    @Test
    @DisplayName("debe filtrar solo por rango de fechas cuando no hay patente")
    public void debeFiltrarSoloPorFechasCuandoNoHayPatente() {
        LocalDate desde = LocalDate.of(2026, 1, 1);
        LocalDate hasta = LocalDate.of(2026, 1, 31);

        when(presupuestoRepository.findByFechaEmisionBetween(desde, hasta)).thenReturn(List.of());

        List<Presupuesto> result = useCase.listar(null, desde, hasta);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(presupuestoRepository).findByFechaEmisionBetween(desde, hasta);
        verify(presupuestoRepository, never()).findByPatente(any());
    }

    @Test
    @DisplayName("debe filtrar solo por patente cuando no hay rango de fechas")
    public void debeFiltrarSoloPorPatenteCuandoNoHayFechas() {
        String patente = "ABC123";
        Vehiculo vehiculo = Vehiculo.builder().patente(patente).build();
        List<Presupuesto> presupuestos = List.of(
                Presupuesto.builder().id(1L).vehiculo(vehiculo).build()
        );

        when(presupuestoRepository.findByPatente(patente)).thenReturn(presupuestos);

        List<Presupuesto> result = useCase.listar(patente, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(presupuestoRepository).findByPatente(patente);
    }

    @Test
    @DisplayName("debe retornar todos los presupuestos cuando no hay patente ni fechas")
    public void debeRetornarTodosCuandoNoHayFiltros() {
        List<Presupuesto> presupuestos = List.of(
                Presupuesto.builder().id(1L).build(),
                Presupuesto.builder().id(2L).build()
        );

        when(presupuestoRepository.findAll()).thenReturn(presupuestos);

        List<Presupuesto> result = useCase.listar(null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(presupuestoRepository).findAll();
    }
}
