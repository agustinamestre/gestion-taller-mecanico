package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.MotivoAlerta;
import com.taller.gestion_taller.domain.model.TipoAlerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.PoliticaServiceVehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerarAlertasServiceUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    private GenerarAlertasServiceUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GenerarAlertasServiceUseCase(vehiculoRepository, alertaRepository,
                new PoliticaServiceVehiculo(12, 10000), ordenTrabajoRepository);
    }

    @Test
    @DisplayName("Debe generar una alerta por kilometraje contando desde el km de alta si nunca tuvo service")
    void debeGenerarAlertaPorKilometrajeDesdeElAlta() {
        LocalDate fechaAlta = LocalDate.now().minusMonths(2);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123")
                .fechaAlta(fechaAlta).kilometrajeAlta(80000).kilometrajeActual(100000).build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, fechaAlta)).thenReturn(false);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(1);
        ArgumentCaptor<Alerta> captor = ArgumentCaptor.forClass(Alerta.class);
        verify(alertaRepository).save(captor.capture());
        assertThat(captor.getValue().getMotivo()).isEqualTo(MotivoAlerta.KILOMETRAJE);
    }

    @Test
    @DisplayName("No debe generar alerta si el vehiculo tiene una orden en curso que incluye service")
    void noDebeGenerarAlertaConOrdenEnCursoConService() {
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123")
                .fechaAlta(LocalDate.now().minusMonths(2)).kilometrajeAlta(80000).kilometrajeActual(100000).build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo));
        when(ordenTrabajoRepository.existsOrdenEnCursoConService(1L)).thenReturn(true);

        int generadas = useCase.generar();

        assertThat(generadas).isZero();
        verify(alertaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar una alerta por tiempo si pasaron 12 meses del ultimo service")
    void debeGenerarAlertaPorTiempo() {
        LocalDate fechaUltimoService = LocalDate.now().minusMonths(13);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123")
                .fechaUltimoService(fechaUltimoService).kilometrajeActual(5000).build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, fechaUltimoService)).thenReturn(false);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(1);
        ArgumentCaptor<Alerta> captor = ArgumentCaptor.forClass(Alerta.class);
        verify(alertaRepository).save(captor.capture());
        Alerta alertaGuardada = captor.getValue();
        assertThat(alertaGuardada.getVehiculoId()).isEqualTo(1L);
        assertThat(alertaGuardada.getTipo()).isEqualTo(TipoAlerta.SERVICE_VENCIDO);
        assertThat(alertaGuardada.getMotivo()).isEqualTo(MotivoAlerta.TIEMPO);
        assertThat(alertaGuardada.isContactado()).isFalse();
    }

    @Test
    @DisplayName("Debe generar una alerta por kilometraje aunque no hayan pasado 12 meses")
    void debeGenerarAlertaPorKilometraje() {
        LocalDate fechaUltimoService = LocalDate.now().minusMonths(4);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123")
                .fechaUltimoService(fechaUltimoService).kmUltimoService(40000).kilometrajeActual(50000).build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, fechaUltimoService)).thenReturn(false);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(1);
        ArgumentCaptor<Alerta> captor = ArgumentCaptor.forClass(Alerta.class);
        verify(alertaRepository).save(captor.capture());
        assertThat(captor.getValue().getMotivo()).isEqualTo(MotivoAlerta.KILOMETRAJE);
    }

    @Test
    @DisplayName("Debe usar la fecha de alta si el vehiculo nunca registro un service")
    void debeUsarFechaDeAltaSiNoHayService() {
        LocalDate fechaAlta = LocalDate.now().minusMonths(12);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123")
                .fechaAlta(fechaAlta).kilometrajeActual(5000).build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, fechaAlta)).thenReturn(false);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(1);
        verify(alertaRepository).existsAlertaVigentePorVehiculo(1L, fechaAlta);
    }

    @Test
    @DisplayName("No debe duplicar la alerta si ya existe una vigente para el vehiculo")
    void noDebeDuplicarAlertaSiYaExisteUnaVigente() {
        LocalDate fechaUltimoService = LocalDate.now().minusMonths(13);
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123").fechaUltimoService(fechaUltimoService).build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo));
        when(alertaRepository.existsAlertaVigentePorVehiculo(1L, fechaUltimoService)).thenReturn(true);

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(0);
        verify(alertaRepository, never()).save(any());
    }

    @Test
    @DisplayName("No debe consultar ni generar alertas para vehiculos con el service al dia")
    void noDebeGenerarAlertaSiElServiceEstaAlDia() {
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123")
                .fechaUltimoService(LocalDate.now().minusMonths(3)).kmUltimoService(40000).kilometrajeActual(45000).build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo));

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(0);
        verify(alertaRepository, never()).existsAlertaVigentePorVehiculo(anyLong(), any());
        verify(alertaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe devolver 0 si no hay vehiculos activos")
    void debeDevolverCeroSiNoHayVehiculos() {
        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of());

        int generadas = useCase.generar();

        assertThat(generadas).isEqualTo(0);
        verify(alertaRepository, never()).save(any());
    }
}
