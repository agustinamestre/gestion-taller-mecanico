package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObtenerOrdenesUseCase")
class ObtenerOrdenesUseCaseTest {

    private static final String PATENTE = "ABC123";

    @Mock private OrdenTrabajoRepository ordenTrabajoRepository;
    @Mock private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ObtenerOrdenesUseCase useCase;

    @Test
    @DisplayName("debe retornar todas las ordenes sin filtros")
    void debeRetornarTodasLasOrdenes() {
        List<OrdenTrabajo> ordenes = List.of(mock(OrdenTrabajo.class), mock(OrdenTrabajo.class));
        when(ordenTrabajoRepository.findByFiltros(null, null)).thenReturn(ordenes);

        List<OrdenTrabajo> resultado = useCase.obtener(null, null);

        assertThat(resultado).hasSize(2);
        verify(vehiculoRepository, never()).findByPatente(any());
    }

    @Test
    @DisplayName("debe retornar ordenes filtradas por patente")
    void debeRetornarOrdenesPorPatente() {
        Vehiculo vehiculo = mock(Vehiculo.class);
        List<OrdenTrabajo> ordenes = List.of(mock(OrdenTrabajo.class));

        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
        when(ordenTrabajoRepository.findByFiltros(PATENTE, null)).thenReturn(ordenes);

        List<OrdenTrabajo> resultado = useCase.obtener(PATENTE, null);

        assertThat(resultado).hasSize(1);
        verify(vehiculoRepository).findByPatente(PATENTE);
    }

    @Test
    @DisplayName("debe retornar ordenes filtradas por estado")
    void debeRetornarOrdenesPorEstado() {
        List<OrdenTrabajo> ordenes = List.of(mock(OrdenTrabajo.class));
        when(ordenTrabajoRepository.findByFiltros(null, EstadoOrdenTrabajo.INGRESADO)).thenReturn(ordenes);

        List<OrdenTrabajo> resultado = useCase.obtener(null, EstadoOrdenTrabajo.INGRESADO);

        assertThat(resultado).hasSize(1);
        verify(vehiculoRepository, never()).findByPatente(any());
    }

    @Test
    @DisplayName("debe retornar ordenes filtradas por patente y estado")
    void debeRetornarOrdenesPorPatenteYEstado() {
        Vehiculo vehiculo = mock(Vehiculo.class);
        List<OrdenTrabajo> ordenes = List.of(mock(OrdenTrabajo.class));

        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
        when(ordenTrabajoRepository.findByFiltros(PATENTE, EstadoOrdenTrabajo.INGRESADO)).thenReturn(ordenes);

        List<OrdenTrabajo> resultado = useCase.obtener(PATENTE, EstadoOrdenTrabajo.INGRESADO);

        assertThat(resultado).hasSize(1);
        verify(vehiculoRepository).findByPatente(PATENTE);
    }

    @Test
    @DisplayName("debe lanzar excepcion si la patente no existe")
    void debeLanzarExcepcionSiPatenteNoExiste() {
        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.obtener(PATENTE, null))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).findByFiltros(any(), any());
    }
}