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
    @DisplayName("debe filtrar por patente y dni cuando estan presentes")
    public void debeFiltrarPorPatenteYDniCuandoEstanPresentes() {
        String patente = "ABC123";
        String dni = "12345678";
        Vehiculo vehiculo = Vehiculo.builder().patente(patente).build();
        List<Presupuesto> presupuestos = List.of(
                Presupuesto.builder().id(1L).vehiculo(vehiculo).dni(dni).build(),
                Presupuesto.builder().id(2L).vehiculo(vehiculo).dni(dni).build()
        );

        when(presupuestoRepository.buscar(patente, dni)).thenReturn(presupuestos);

        List<Presupuesto> result = useCase.listar(patente, dni);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(presupuestoRepository).buscar(patente, dni);
    }

    @Test
    @DisplayName("debe filtrar solo por dni cuando no hay patente")
    public void debeFiltrarSoloPorDniCuandoNoHayPatente() {
        String dni = "12345678";
        Presupuesto presupuesto = Presupuesto.builder().id(1L).dni(dni).build();

        when(presupuestoRepository.buscar(null, dni)).thenReturn(List.of(presupuesto));

        List<Presupuesto> result = useCase.listar(null, dni);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(presupuestoRepository).buscar(null, dni);
    }

    @Test
    @DisplayName("debe filtrar solo por patente cuando no hay dni")
    public void debeFiltrarSoloPorPatenteCuandoNoHayDni() {
        String patente = "ABC123";
        Vehiculo vehiculo = Vehiculo.builder().patente(patente).build();
        List<Presupuesto> presupuestos = List.of(
                Presupuesto.builder().id(1L).vehiculo(vehiculo).build()
        );

        when(presupuestoRepository.buscar(patente, null)).thenReturn(presupuestos);

        List<Presupuesto> result = useCase.listar(patente, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(presupuestoRepository).buscar(patente, null);
    }

    @Test
    @DisplayName("debe retornar todos los presupuestos cuando no hay ningun filtro")
    public void debeRetornarTodosCuandoNoHayFiltros() {
        List<Presupuesto> presupuestos = List.of(
                Presupuesto.builder().id(1L).build(),
                Presupuesto.builder().id(2L).build()
        );

        when(presupuestoRepository.findAll()).thenReturn(presupuestos);

        List<Presupuesto> result = useCase.listar(null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(presupuestoRepository).findAll();
        verify(presupuestoRepository, never()).buscar(any(), any());
    }
}
