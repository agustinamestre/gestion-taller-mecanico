package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Presupuesto (agregado)")
class PresupuestoTest {

    private static Presupuesto presupuestoPendiente(Vehiculo vehiculo) {
        return Presupuesto.builder()
                .id(1L)
                .vehiculo(vehiculo)
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>())
                .build();
    }

    @Nested
    @DisplayName("cambiarEstado")
    class CambiarEstado {

        @Test
        @DisplayName("permite aprobar cuando tiene vehiculo asociado")
        void permiteAprobarConVehiculo() {
            Presupuesto presupuesto = presupuestoPendiente(mock(Vehiculo.class));

            presupuesto.cambiarEstado(EstadoPresupuesto.APROBADO);

            assertThat(presupuesto.getEstado()).isEqualTo(EstadoPresupuesto.APROBADO);
        }

        @Test
        @DisplayName("NO permite aprobar cuando no tiene vehiculo asociado")
        void rechazaAprobarSinVehiculo() {
            Presupuesto presupuesto = presupuestoPendiente(null);

            assertThatThrownBy(() -> presupuesto.cambiarEstado(EstadoPresupuesto.APROBADO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("PRESUPUESTO_SIN_VEHICULO");

            assertThat(presupuesto.getEstado())
                    .as("El estado no debe cambiar si la validacion falla")
                    .isEqualTo(EstadoPresupuesto.PENDIENTE);
        }

        @Test
        @DisplayName("permite rechazar aunque no tenga vehiculo")
        void permiteRechazarSinVehiculo() {
            Presupuesto presupuesto = presupuestoPendiente(null);

            presupuesto.cambiarEstado(EstadoPresupuesto.RECHAZADO);

            assertThat(presupuesto.getEstado()).isEqualTo(EstadoPresupuesto.RECHAZADO);
        }

        @Test
        @DisplayName("NO permite vencer via cambiarEstado (solo el job de vencimiento puede hacerlo)")
        void rechazaVencerViaTransicionGenerica() {
            Presupuesto presupuesto = presupuestoPendiente(null);

            assertThatThrownBy(() -> presupuesto.cambiarEstado(EstadoPresupuesto.VENCIDO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("TRANSICION_ESTADO_INVALIDA");

            assertThat(presupuesto.getEstado()).isEqualTo(EstadoPresupuesto.PENDIENTE);
        }

        @Test
        @DisplayName("rechaza transiciones invalidas (ej: APROBADO -> RECHAZADO)")
        void rechazaTransicionInvalida() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(1L)
                    .vehiculo(mock(Vehiculo.class))
                    .estado(EstadoPresupuesto.APROBADO)
                    .items(new ArrayList<>())
                    .build();

            assertThatThrownBy(() -> presupuesto.cambiarEstado(EstadoPresupuesto.RECHAZADO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("TRANSICION_ESTADO_INVALIDA");
        }

        @Test
        @DisplayName("la validacion de transicion tiene prioridad sobre la invariante de vehiculo")
        void prioridadTransicionInvalidaSobreInvariante() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(1L)
                    .vehiculo(null)
                    .estado(EstadoPresupuesto.APROBADO)
                    .items(new ArrayList<>())
                    .build();

            assertThatThrownBy(() -> presupuesto.cambiarEstado(EstadoPresupuesto.APROBADO))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("TRANSICION_ESTADO_INVALIDA");
        }
    }

    @Nested
    @DisplayName("marcarComoVencido")
    class MarcarComoVencido {

        @Test
        @DisplayName("vence un presupuesto PENDIENTE cuya fecha de vencimiento ya paso")
        void venceUnPendienteConFechaVencida() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(1L)
                    .vehiculo(null)
                    .estado(EstadoPresupuesto.PENDIENTE)
                    .fechaVencimiento(java.time.LocalDate.now().minusDays(1))
                    .items(new ArrayList<>())
                    .build();

            presupuesto.marcarComoVencido();

            assertThat(presupuesto.getEstado()).isEqualTo(EstadoPresupuesto.VENCIDO);
        }

        @Test
        @DisplayName("rechaza vencer si la fecha de vencimiento aun no llego")
        void rechazaSiFechaAunNoVencio() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(1L)
                    .vehiculo(null)
                    .estado(EstadoPresupuesto.PENDIENTE)
                    .fechaVencimiento(java.time.LocalDate.now().plusDays(1))
                    .items(new ArrayList<>())
                    .build();

            assertThatThrownBy(presupuesto::marcarComoVencido)
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("PRESUPUESTO_AUN_NO_VENCIDO");

            assertThat(presupuesto.getEstado()).isEqualTo(EstadoPresupuesto.PENDIENTE);
        }

        @Test
        @DisplayName("rechaza vencer un presupuesto que no esta PENDIENTE ni APROBADO (ej: RECHAZADO)")
        void rechazaSiNoEstaPendiente() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(1L)
                    .vehiculo(mock(Vehiculo.class))
                    .estado(EstadoPresupuesto.RECHAZADO)
                    .fechaVencimiento(java.time.LocalDate.now().minusDays(1))
                    .items(new ArrayList<>())
                    .build();

            assertThatThrownBy(presupuesto::marcarComoVencido)
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("TRANSICION_ESTADO_INVALIDA");

            assertThat(presupuesto.getEstado()).isEqualTo(EstadoPresupuesto.RECHAZADO);
        }
    }

    @Nested
    @DisplayName("asociarVehiculo")
    class AsociarVehiculo {

        @Test
        @DisplayName("asocia el vehiculo cuando el presupuesto esta PENDIENTE y no tiene vehiculo")
        void asociaVehiculoExitosamente() {
            Presupuesto presupuesto = presupuestoPendiente(null);
            Cliente cliente = mock(Cliente.class);
            when(cliente.getDni()).thenReturn("12345678");
            Vehiculo vehiculo = mock(Vehiculo.class);
            when(vehiculo.getCliente()).thenReturn(cliente);

            presupuesto.asociarVehiculo(vehiculo);

            assertThat(presupuesto.getVehiculo()).isSameAs(vehiculo);
        }

        @Test
        @DisplayName("lanza NullPointerException si el vehiculo es nulo")
        void rechazaVehiculoNulo() {
            Presupuesto presupuesto = presupuestoPendiente(null);

            assertThatThrownBy(() -> presupuesto.asociarVehiculo(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("rechaza asociar si el presupuesto ya tiene vehiculo")
        void rechazaSiYaTieneVehiculo() {
            Vehiculo vehiculoOriginal = mock(Vehiculo.class);
            Vehiculo otroVehiculo = mock(Vehiculo.class);
            Presupuesto presupuesto = presupuestoPendiente(vehiculoOriginal);

            assertThatThrownBy(() -> presupuesto.asociarVehiculo(otroVehiculo))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("PRESUPUESTO_YA_TIENE_VEHICULO");

            assertThat(presupuesto.getVehiculo()).isSameAs(vehiculoOriginal);
        }

        @Test
        @DisplayName("rechaza asociar si el presupuesto no esta PENDIENTE")
        void rechazaSiNoEstaPendiente() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(1L)
                    .vehiculo(null)
                    .estado(EstadoPresupuesto.RECHAZADO)
                    .items(new ArrayList<>())
                    .build();
            Vehiculo vehiculo = mock(Vehiculo.class);

            assertThatThrownBy(() -> presupuesto.asociarVehiculo(vehiculo))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("PRESUPUESTO_NO_PENDIENTE");

            assertThat(presupuesto.getVehiculo()).isNull();
        }

        @Test
        @DisplayName("asocia el vehiculo y toma el dni del cliente cuando el presupuesto no tenia dni")
        void asociaYCompletaDniDesdeCliente() {
            Presupuesto presupuesto = presupuestoPendiente(null);
            Cliente cliente = mock(Cliente.class);
            when(cliente.getDni()).thenReturn("12345678");
            Vehiculo vehiculo = mock(Vehiculo.class);
            when(vehiculo.getCliente()).thenReturn(cliente);

            presupuesto.asociarVehiculo(vehiculo);

            assertThat(presupuesto.getVehiculo()).isSameAs(vehiculo);
            assertThat(presupuesto.getDni()).isEqualTo("12345678");
        }

        @Test
        @DisplayName("rechaza asociar si el dni existente no coincide con el del cliente titular del vehiculo")
        void rechazaSiDniNoCoincideConCliente() {
            Presupuesto presupuesto = Presupuesto.builder()
                    .id(1L)
                    .vehiculo(null)
                    .dni("11111111")
                    .estado(EstadoPresupuesto.PENDIENTE)
                    .items(new ArrayList<>())
                    .build();
            Cliente cliente = mock(Cliente.class);
            when(cliente.getDni()).thenReturn("22222222");
            Vehiculo vehiculo = mock(Vehiculo.class);
            when(vehiculo.getCliente()).thenReturn(cliente);

            assertThatThrownBy(() -> presupuesto.asociarVehiculo(vehiculo))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("DNI_INCONSISTENTE_CON_VEHICULO");

            assertThat(presupuesto.getVehiculo()).isNull();
        }
    }

    @Nested
    @DisplayName("crearNuevo")
    class CrearNuevo {

        @Test
        @DisplayName("rechaza crear sin vehiculo y sin dni")
        void rechazaSinVehiculoYSinDni() {
            assertThatThrownBy(() -> Presupuesto.crearNuevo(null, null, "obs"))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("PRESUPUESTO_SIN_DNI");
        }

        @Test
        @DisplayName("rechaza crear sin vehiculo y con dni en blanco")
        void rechazaSinVehiculoYConDniBlanco() {
            assertThatThrownBy(() -> Presupuesto.crearNuevo(null, "   ", "obs"))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("PRESUPUESTO_SIN_DNI");
        }

        @Test
        @DisplayName("crea el presupuesto sin vehiculo cuando se envia dni")
        void creaSinVehiculoConDni() {
            Presupuesto presupuesto = Presupuesto.crearNuevo(null, "12345678", "obs");

            assertThat(presupuesto.getVehiculo()).isNull();
            assertThat(presupuesto.getDni()).isEqualTo("12345678");
        }

        @Test
        @DisplayName("autocompleta el dni con el del cliente del vehiculo cuando no se envia dni")
        void autocompletaDniDesdeVehiculoCuandoNoSeEnviaDni() {
            Cliente cliente = mock(Cliente.class);
            when(cliente.getDni()).thenReturn("87654321");
            Vehiculo vehiculo = mock(Vehiculo.class);
            when(vehiculo.getCliente()).thenReturn(cliente);

            Presupuesto presupuesto = Presupuesto.crearNuevo(vehiculo, null, "obs");

            assertThat(presupuesto.getDni()).isEqualTo("87654321");
        }

        @Test
        @DisplayName("permite crear con vehiculo cuando el dni enviado coincide con el del cliente")
        void permiteCrearConVehiculoYDniCoincidente() {
            Cliente cliente = mock(Cliente.class);
            when(cliente.getDni()).thenReturn("87654321");
            Vehiculo vehiculo = mock(Vehiculo.class);
            when(vehiculo.getCliente()).thenReturn(cliente);

            Presupuesto presupuesto = Presupuesto.crearNuevo(vehiculo, "87654321", "obs");

            assertThat(presupuesto.getDni()).isEqualTo("87654321");
        }

        @Test
        @DisplayName("rechaza crear con vehiculo cuando el dni enviado no coincide con el del cliente")
        void rechazaCrearConVehiculoYDniNoCoincidente() {
            Cliente cliente = mock(Cliente.class);
            when(cliente.getDni()).thenReturn("87654321");
            Vehiculo vehiculo = mock(Vehiculo.class);
            when(vehiculo.getCliente()).thenReturn(cliente);

            assertThatThrownBy(() -> Presupuesto.crearNuevo(vehiculo, "11111111", "obs"))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("DNI_INCONSISTENTE_CON_VEHICULO");
        }
    }
}
