package com.taller.gestion_taller.application.usecases.alerta;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerarAlertasServiceUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private GenerarAlertasServiceUseCase useCase;

    @Test
    @DisplayName("Debe generar una alerta por cada vehiculo vencido sin alerta vigente")
    void debeGenerarAlertasParaVehiculosVencidosSinAlertaVigente() {
        LocalDate fechaUltimoService = LocalDate.now().minusMonths(13);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123").fechaUltimoService(fechaUltimoService).build();

        when(vehiculoRepository.findByActivoTrueAndFechaUltimoServiceBefore(any())).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, fechaUltimoService)).thenReturn(false);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(1);
        ArgumentCaptor<Alerta> captor = ArgumentCaptor.forClass(Alerta.class);
        verify(alertaRepository).save(captor.capture());
        Alerta alertaGuardada = captor.getValue();
        assertThat(alertaGuardada.getVehiculoId()).isEqualTo(1L);
        assertThat(alertaGuardada.getTipo()).isEqualTo(TipoAlerta.SERVICE_VENCIDO);
        assertThat(alertaGuardada.isContactado()).isFalse();
    }

    @Test
    @DisplayName("No debe duplicar la alerta si ya existe una vigente para el vehiculo")
    void noDebeDuplicarAlertaSiYaExisteUnaVigente() {
        LocalDate fechaUltimoService = LocalDate.now().minusMonths(13);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123").fechaUltimoService(fechaUltimoService).build();

        when(vehiculoRepository.findByActivoTrueAndFechaUltimoServiceBefore(any())).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, fechaUltimoService)).thenReturn(true);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(0);
        verify(alertaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar una nueva alerta si el vehiculo se le actualizo la fecha de ultimo service y volvio a vencerse")
    void debeGenerarNuevaAlertaSiSeActualizoFechaUltimoServiceYVolvioAVencerse() {
        LocalDate nuevaFechaUltimoService = LocalDate.now().minusMonths(13);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123").fechaUltimoService(nuevaFechaUltimoService).build();

        when(vehiculoRepository.findByActivoTrueAndFechaUltimoServiceBefore(any())).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, nuevaFechaUltimoService)).thenReturn(false);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(1);
        verify(alertaRepository, times(1)).save(any());
        verify(alertaRepository).existsAlertaVigentePorVehiculo(1L, nuevaFechaUltimoService);
    }

    @Test
    @DisplayName("Debe devolver 0 y no consultar alertas si no hay vehiculos vencidos")
    void debeDevolverCeroSiNoHayVehiculosVencidos() {
        when(vehiculoRepository.findByActivoTrueAndFechaUltimoServiceBefore(any())).thenReturn(List.of());

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(0);
        verify(alertaRepository, never()).existsAlertaVigentePorVehiculo(anyLong(), any());
        verify(alertaRepository, never()).save(any());
    }
}
