package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.TipoAlerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarAlertasConVehiculoUseCaseTest {

    private static final String PATENTE = "AB123CD";
    private static final Boolean CONTACTADO = false;

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ListarAlertasConVehiculoUseCase useCase;

    private static Alerta alerta(Long id, Long vehiculoId) {
        return Alerta.builder()
                .id(id)
                .vehiculoId(vehiculoId)
                .tipo(TipoAlerta.SERVICE_VENCIDO)
                .fechaAlerta(LocalDate.now())
                .contactado(false)
                .build();
    }

    private static Vehiculo vehiculo(Long id) {
        return Vehiculo.builder()
                .id(id)
                .patente("AB123CD")
                .build();
    }

    @Test
    @DisplayName("Debe devolver las alertas emparejadas con su vehiculo correspondiente")
    void debeDevolverAlertasEmparejadasConSuVehiculo() {
        Alerta alerta1 = alerta(1L, 10L);
        Alerta alerta2 = alerta(2L, 20L);
        Vehiculo vehiculo1 = vehiculo(10L);
        Vehiculo vehiculo2 = vehiculo(20L);

        when(alertaRepository.findByFiltros(PATENTE, CONTACTADO)).thenReturn(List.of(alerta1, alerta2));
        when(vehiculoRepository.findByIdIn(List.of(10L, 20L))).thenReturn(List.of(vehiculo1, vehiculo2));

        List<AlertaConVehiculo> resultado = useCase.listar(PATENTE, CONTACTADO);

        assertThat(resultado)
                .containsExactly(new AlertaConVehiculo(alerta1, vehiculo1), new AlertaConVehiculo(alerta2, vehiculo2));
    }

    @Test
    @DisplayName("Debe deduplicar los ids de vehiculo cuando varias alertas comparten el mismo vehiculo")
    void debeDeduplicarIdsDeVehiculoRepetidos() {
        Alerta alerta1 = alerta(1L, 10L);
        Alerta alerta2 = alerta(2L, 10L);
        Vehiculo vehiculo1 = vehiculo(10L);

        when(alertaRepository.findByFiltros(PATENTE, CONTACTADO)).thenReturn(List.of(alerta1, alerta2));
        when(vehiculoRepository.findByIdIn(List.of(10L))).thenReturn(List.of(vehiculo1));

        List<AlertaConVehiculo> resultado = useCase.listar(PATENTE, CONTACTADO);

        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass(List.class);
        verify(vehiculoRepository).findByIdIn(idsCaptor.capture());
        assertThat(idsCaptor.getValue()).containsExactly(10L);
        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("Debe devolver una lista vacia cuando no hay alertas")
    void debeDevolverListaVaciaCuandoNoHayAlertas() {
        when(alertaRepository.findByFiltros(PATENTE, CONTACTADO)).thenReturn(List.of());
        when(vehiculoRepository.findByIdIn(List.of())).thenReturn(List.of());

        List<AlertaConVehiculo> resultado = useCase.listar(PATENTE, CONTACTADO);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException cuando el vehiculo de una alerta no se encuentra")
    void debeLanzarNotFoundExceptionCuandoElVehiculoNoSeEncuentra() {
        Alerta alerta1 = alerta(1L, 10L);

        when(alertaRepository.findByFiltros(PATENTE, CONTACTADO)).thenReturn(List.of(alerta1));
        when(vehiculoRepository.findByIdIn(List.of(10L))).thenReturn(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> useCase.listar(PATENTE, CONTACTADO));

        assertEquals("VEHICULO_NO_ENCONTRADO", exception.getBusinessError().code());
    }

    @Test
    @DisplayName("Debe pasar los filtros de patente y contactado al repositorio de alertas")
    void debePasarLosFiltrosAlRepositorioDeAlertas() {
        when(alertaRepository.findByFiltros(PATENTE, CONTACTADO)).thenReturn(List.of());
        when(vehiculoRepository.findByIdIn(List.of())).thenReturn(List.of());

        useCase.listar(PATENTE, CONTACTADO);

        verify(alertaRepository).findByFiltros(PATENTE, CONTACTADO);
    }
}
