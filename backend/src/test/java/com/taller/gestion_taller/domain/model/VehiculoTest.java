package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@DisplayName("Vehiculo (agregado)")
class VehiculoTest {

    private static final String PATENTE = "ABC123";
    private static final Integer ANIO = 2020;
    private static final Integer KM = 50_000;

    @Nested
    @DisplayName("crearNuevo (factory)")
    class CrearNuevo {

        @Test
        @DisplayName("crea un vehiculo activo cuando todos los datos son validos")
        void creaVehiculoValido() {
            Modelo modelo = mock(Modelo.class);
            Cliente cliente = mock(Cliente.class);

            Vehiculo vehiculo = Vehiculo.crearNuevo(PATENTE, modelo, ANIO, cliente, KM);

            assertThat(vehiculo.getPatente()).isEqualTo(PATENTE);
            assertThat(vehiculo.getModelo()).isSameAs(modelo);
            assertThat(vehiculo.getCliente()).isSameAs(cliente);
            assertThat(vehiculo.getAnio()).isEqualTo(ANIO);
            assertThat(vehiculo.getKilometrajeActual()).isEqualTo(KM);
            assertThat(vehiculo.isActivo()).isTrue();
            assertThat(vehiculo.getId()).isNull();
        }

        @Test
        @DisplayName("rechaza cliente nulo")
        void rechazaClienteNulo() {
            assertThatThrownBy(() ->
                    Vehiculo.crearNuevo(PATENTE, mock(Modelo.class), ANIO, null, KM))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_SIN_CLIENTE");
        }

        @Test
        @DisplayName("rechaza modelo nulo")
        void rechazaModeloNulo() {
            assertThatThrownBy(() ->
                    Vehiculo.crearNuevo(PATENTE, null, ANIO, mock(Cliente.class), KM))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_SIN_MODELO");
        }

        @Test
        @DisplayName("rechaza patente nula")
        void rechazaPatenteNula() {
            assertThatThrownBy(() ->
                    Vehiculo.crearNuevo(null, mock(Modelo.class), ANIO, mock(Cliente.class), KM))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_SIN_PATENTE");
        }

        @Test
        @DisplayName("rechaza patente en blanco")
        void rechazaPatenteEnBlanco() {
            assertThatThrownBy(() ->
                    Vehiculo.crearNuevo("   ", mock(Modelo.class), ANIO, mock(Cliente.class), KM))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_SIN_PATENTE");
        }
    }

    @Nested
    @DisplayName("actualizarDatos")
    class ActualizarDatos {

        @Test
        @DisplayName("actualiza modelo, anio y cliente devolviendo nueva instancia")
        void actualizaDatos() {
            Vehiculo original = Vehiculo.crearNuevo(
                    PATENTE, mock(Modelo.class), ANIO, mock(Cliente.class), KM);
            Modelo nuevoModelo = mock(Modelo.class);
            Cliente nuevoCliente = mock(Cliente.class);

            Vehiculo actualizado = original.actualizarDatos(nuevoModelo, 2024, nuevoCliente);

            assertThat(actualizado).isNotSameAs(original);
            assertThat(actualizado.getModelo()).isSameAs(nuevoModelo);
            assertThat(actualizado.getCliente()).isSameAs(nuevoCliente);
            assertThat(actualizado.getAnio()).isEqualTo(2024);
            assertThat(actualizado.getPatente()).isEqualTo(PATENTE);
        }

        @Test
        @DisplayName("rechaza cliente nulo (no se puede dejar el vehiculo huerfano)")
        void rechazaClienteNulo() {
            Vehiculo vehiculo = Vehiculo.crearNuevo(
                    PATENTE, mock(Modelo.class), ANIO, mock(Cliente.class), KM);

            assertThatThrownBy(() -> vehiculo.actualizarDatos(mock(Modelo.class), 2024, null))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_SIN_CLIENTE");
        }

        @Test
        @DisplayName("rechaza modelo nulo")
        void rechazaModeloNulo() {
            Vehiculo vehiculo = Vehiculo.crearNuevo(
                    PATENTE, mock(Modelo.class), ANIO, mock(Cliente.class), KM);

            assertThatThrownBy(() -> vehiculo.actualizarDatos(null, 2024, mock(Cliente.class)))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_SIN_MODELO");
        }
    }

    @Nested
    @DisplayName("actualizarKilometraje")
    class ActualizarKilometraje {

        @Test
        @DisplayName("permite incrementar el kilometraje")
        void permiteIncrementar() {
            Vehiculo vehiculo = Vehiculo.crearNuevo(
                    PATENTE, mock(Modelo.class), ANIO, mock(Cliente.class), KM);

            Vehiculo actualizado = vehiculo.actualizarKilometraje(KM + 1000);

            assertThat(actualizado.getKilometrajeActual()).isEqualTo(KM + 1000);
        }

        @Test
        @DisplayName("rechaza disminuir el kilometraje")
        void rechazaDisminuir() {
            Vehiculo vehiculo = Vehiculo.crearNuevo(
                    PATENTE, mock(Modelo.class), ANIO, mock(Cliente.class), KM);

            assertThatThrownBy(() -> vehiculo.actualizarKilometraje(KM - 1))
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("KILOMETRAJEACTUAL_INVALIDO");
        }
    }

    @Nested
    @DisplayName("desactivar")
    class Desactivar {

        @Test
        @DisplayName("desactiva un vehiculo activo")
        void desactivaActivo() {
            Vehiculo vehiculo = Vehiculo.crearNuevo(
                    PATENTE, mock(Modelo.class), ANIO, mock(Cliente.class), KM);

            Vehiculo desactivado = vehiculo.desactivar();

            assertThat(desactivado.isActivo()).isFalse();
        }

        @Test
        @DisplayName("rechaza desactivar un vehiculo ya desactivado")
        void rechazaSiYaEstaDesactivado() {
            Vehiculo vehiculo = Vehiculo.crearNuevo(
                            PATENTE, mock(Modelo.class), ANIO, mock(Cliente.class), KM)
                    .desactivar();

            assertThatThrownBy(vehiculo::desactivar)
                    .isInstanceOf(BusinessRunTimeException.class)
                    .extracting("businessError.code")
                    .isEqualTo("VEHICULO_YA_DESACTIVADO");
        }
    }
}
