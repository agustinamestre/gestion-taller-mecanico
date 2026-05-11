package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObtenerPresupuestosPorPatenteUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ObtenerPresupuestosPorPatenteUseCase obtenerPresupuestosPorPatenteUseCase;

    @Test
    @DisplayName("debe retornar los presupuestos si la patente existe")
    public void debeRetornarPresupuestosCuandoLaPatenteExiste() {
        String patente = "ABC123";
        Vehiculo vehiculo = Vehiculo.builder().patente(patente).build();
        List<Presupuesto> presupuestos = List.of(
                Presupuesto.builder().id(1L).vehiculo(vehiculo).build(),
                Presupuesto.builder().id(2L).vehiculo(vehiculo).build()
        );

        when(vehiculoRepository.findByPatente(patente)).thenReturn(Optional.of(vehiculo));
        when(presupuestoRepository.findByPatente(patente)).thenReturn(presupuestos);

        List<Presupuesto> result = obtenerPresupuestosPorPatenteUseCase.obtener(patente);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(presupuestoRepository).findByPatente(patente);
    }

    @Test
    @DisplayName("debe retornar una lista vacia si no hay presupuestos asociados al vehiculo")
    public void debeRetornarUnaListaVaciaCuandoElVehiculoExistePeroNoTienePresupuestos() {
        String patente = "ABC123";
        Vehiculo vehiculo = Vehiculo.builder().patente(patente).build();

        when(vehiculoRepository.findByPatente(patente)).thenReturn(Optional.of(vehiculo));
        when(presupuestoRepository.findByPatente(patente)).thenReturn(List.of());

        List<Presupuesto> result = obtenerPresupuestosPorPatenteUseCase.obtener(patente);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("debe retornar NotFoundException si la patente no existe")
    public void debeRetornarNotFoundExceptionCuandoLaPatenteNoExiste() {
        String patente = "XYZ999";

        when(vehiculoRepository.findByPatente(patente)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                obtenerPresupuestosPorPatenteUseCase.obtener(patente)
        );

        assertEquals("VEHICULO_NO_ENCONTRADO", exception.getBusinessError().code());

        verify(presupuestoRepository, never()).findByPatente(any());
    }
}