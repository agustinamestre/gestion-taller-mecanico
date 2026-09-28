package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarOrdenTrabajoUseCase")
class RegistrarOrdenTrabajoUseCaseTest {

    private static final String PATENTE = "ABC123";
    private static final Long USUARIO_ID = 1L;
    private static final Long PRESUPUESTO_ID = 1L;
    private static final String DESCRIPCION = "Ruido en el motor";

    private static final RegistrarOrdenTrabajoCommand COMMAND_CON_PRESUPUESTO =
            new RegistrarOrdenTrabajoCommand(PATENTE, PRESUPUESTO_ID, DESCRIPCION, USUARIO_ID);

    private static final RegistrarOrdenTrabajoCommand COMMAND_SIN_PRESUPUESTO =
            new RegistrarOrdenTrabajoCommand(PATENTE, null, DESCRIPCION, USUARIO_ID);

    @Mock private OrdenTrabajoRepository ordenTrabajoRepository;
    @Mock private VehiculoRepository vehiculoRepository;
    @Mock private PresupuestoRepository presupuestoRepository;

    private RegistrarOrdenTrabajoUseCase useCase;

    private Vehiculo vehiculo;
    private OrdenTrabajo ordenGuardada;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarOrdenTrabajoUseCase(ordenTrabajoRepository, vehiculoRepository, presupuestoRepository);
        vehiculo = Vehiculo.builder().id(1L).patente(PATENTE).build();
        ordenGuardada = OrdenTrabajo.builder().id(10L).build();
    }

    private OrdenTrabajo ordenConEstado(EstadoOrdenTrabajo estado) {
        return OrdenTrabajo.builder().id(99L).estado(estado).build();
    }

    @Nested
    @DisplayName("Validacion de vehiculo con orden activa")
    class ValidacionOrdenActiva {

        @Test
        @DisplayName("permite registrar sin presupuesto cuando el vehiculo no tiene ninguna orden previa")
        void permiteCuandoNoHayOrdenesPrevias() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null)).thenReturn(List.of());
            when(ordenTrabajoRepository.save(any())).thenReturn(ordenGuardada);

            OrdenTrabajo resultado = useCase.registrar(COMMAND_SIN_PRESUPUESTO);

            assertThat(resultado).isEqualTo(ordenGuardada);
        }

        @Test
        @DisplayName("rechaza registrar sin presupuesto cuando el vehiculo ya tiene una orden INGRESADO")
        void rechazaCuandoYaTieneOrdenIngresada() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null))
                    .thenReturn(List.of(ordenConEstado(EstadoOrdenTrabajo.INGRESADO)));

            assertThatThrownBy(() -> useCase.registrar(COMMAND_SIN_PRESUPUESTO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_CON_ORDEN_ACTIVA");

            verify(ordenTrabajoRepository, never()).save(any());
        }

        @Test
        @DisplayName("rechaza registrar sin presupuesto cuando el vehiculo ya tiene una orden EN_REPARACION")
        void rechazaCuandoYaTieneOrdenEnReparacion() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null))
                    .thenReturn(List.of(ordenConEstado(EstadoOrdenTrabajo.EN_REPARACION)));

            assertThatThrownBy(() -> useCase.registrar(COMMAND_SIN_PRESUPUESTO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_CON_ORDEN_ACTIVA");

            verify(ordenTrabajoRepository, never()).save(any());
        }

        @Test
        @DisplayName("rechaza registrar sin presupuesto cuando el vehiculo ya tiene una orden FINALIZADO (aun no entregada)")
        void rechazaCuandoYaTieneOrdenFinalizada() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null))
                    .thenReturn(List.of(ordenConEstado(EstadoOrdenTrabajo.FINALIZADO)));

            assertThatThrownBy(() -> useCase.registrar(COMMAND_SIN_PRESUPUESTO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_CON_ORDEN_ACTIVA");
        }

        @Test
        @DisplayName("permite registrar sin presupuesto cuando la unica orden previa del vehiculo esta ENTREGADA")
        void permiteCuandoLaOrdenPreviaFueEntregada() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null))
                    .thenReturn(List.of(ordenConEstado(EstadoOrdenTrabajo.ENTREGADO)));
            when(ordenTrabajoRepository.save(any())).thenReturn(ordenGuardada);

            OrdenTrabajo resultado = useCase.registrar(COMMAND_SIN_PRESUPUESTO);

            assertThat(resultado).isEqualTo(ordenGuardada);
        }

        @Test
        @DisplayName("permite registrar sin presupuesto cuando la unica orden previa del vehiculo esta CANCELADA")
        void permiteCuandoLaOrdenPreviaFueCancelada() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null))
                    .thenReturn(List.of(ordenConEstado(EstadoOrdenTrabajo.CANCELADO)));
            when(ordenTrabajoRepository.save(any())).thenReturn(ordenGuardada);

            OrdenTrabajo resultado = useCase.registrar(COMMAND_SIN_PRESUPUESTO);

            assertThat(resultado).isEqualTo(ordenGuardada);
        }

        @Test
        @DisplayName("rechaza registrar desde presupuesto cuando el vehiculo del presupuesto ya tiene una orden activa")
        void rechazaDesdePresupuestoCuandoVehiculoTieneOrdenActiva() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(PRESUPUESTO_ID)
                    .estado(EstadoPresupuesto.APROBADO)
                    .vehiculo(vehiculo)
                    .build();

            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null))
                    .thenReturn(List.of(ordenConEstado(EstadoOrdenTrabajo.INGRESADO)));

            assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_CON_ORDEN_ACTIVA");

            verify(ordenTrabajoRepository, never()).save(any());
            verify(presupuestoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Cuando se registra con presupuesto")
    class ConPresupuesto {

        @Test
        @DisplayName("debe registrar la orden cuando el presupuesto esta APROBADO, tiene vehiculo y no hay ordenes previas")
        void registraOrdenConPresupuestoAprobado() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(PRESUPUESTO_ID)
                    .estado(EstadoPresupuesto.APROBADO)
                    .vehiculo(vehiculo)
                    .build();

            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null)).thenReturn(List.of());
            when(ordenTrabajoRepository.save(any())).thenReturn(ordenGuardada);

            OrdenTrabajo resultado = useCase.registrar(COMMAND_CON_PRESUPUESTO);

            assertThat(resultado).isEqualTo(ordenGuardada);
            verify(presupuestoRepository).save(presupuesto);
            verifyNoInteractions(vehiculoRepository);
        }

        @Test
        @DisplayName("debe lanzar BusinessRunTimeException cuando la patente del comando NO coincide con la del presupuesto")
        void lanzaExcepcionCuandoPatenteInconsistente() {
            RegistrarOrdenTrabajoCommand commandPatenteDistinta =
                    new RegistrarOrdenTrabajoCommand("XYZ999", PRESUPUESTO_ID, DESCRIPCION, USUARIO_ID);
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(PRESUPUESTO_ID)
                    .estado(EstadoPresupuesto.APROBADO)
                    .vehiculo(vehiculo)
                    .build();

            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));

            assertThatThrownBy(() -> useCase.registrar(commandPatenteDistinta))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("PATENTE_INCONSISTENTE_CON_PRESUPUESTO");

            verify(ordenTrabajoRepository, never()).save(any());
            verifyNoInteractions(vehiculoRepository);
        }

        @Test
        @DisplayName("debe lanzar NotFoundException cuando el presupuesto no existe")
        void lanzaNotFoundCuandoPresupuestoNoExiste() {
            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
                    .isInstanceOf(NotFoundException.class);

            verify(ordenTrabajoRepository, never()).save(any());
            verifyNoInteractions(vehiculoRepository);
        }

        @Test
        @DisplayName("debe lanzar BusinessRunTimeException cuando el presupuesto no tiene vehiculo asociado")
        void lanzaExcepcionCuandoPresupuestoSinVehiculo() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(PRESUPUESTO_ID)
                    .estado(EstadoPresupuesto.APROBADO)
                    .vehiculo(null)
                    .build();

            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));

            assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .hasMessageContaining("vehículo");

            verify(ordenTrabajoRepository, never()).save(any());
            verifyNoInteractions(vehiculoRepository);
        }

        @Test
        @DisplayName("debe lanzar BusinessRunTimeException cuando el presupuesto no esta APROBADO")
        void lanzaExcepcionCuandoPresupuestoNoAprobado() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(PRESUPUESTO_ID)
                    .estado(EstadoPresupuesto.PENDIENTE)
                    .vehiculo(vehiculo)
                    .build();

            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));

            assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
                    .isInstanceOf(BusinessRunTimeException.class);

            verify(ordenTrabajoRepository, never()).save(any());
            verifyNoInteractions(vehiculoRepository);
        }
    }

    @Nested
    @DisplayName("Cuando se registra sin presupuesto")
    class SinPresupuesto {

        @Test
        @DisplayName("debe registrar la orden buscando el vehiculo por patente")
        void registraOrdenPorPatente() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
            when(ordenTrabajoRepository.findByFiltros(PATENTE, null)).thenReturn(List.of());
            when(ordenTrabajoRepository.save(any())).thenReturn(ordenGuardada);

            OrdenTrabajo resultado = useCase.registrar(COMMAND_SIN_PRESUPUESTO);

            assertThat(resultado).isEqualTo(ordenGuardada);
            verify(ordenTrabajoRepository).save(any());
            verifyNoInteractions(presupuestoRepository);
        }

        @Test
        @DisplayName("debe lanzar NotFoundException cuando el vehiculo no existe")
        void lanzaNotFoundCuandoVehiculoNoExiste() {
            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.registrar(COMMAND_SIN_PRESUPUESTO))
                    .isInstanceOf(NotFoundException.class);

            verify(ordenTrabajoRepository, never()).save(any());
            verifyNoInteractions(presupuestoRepository);
        }
    }
}
