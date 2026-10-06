package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.model.MotivoAlerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PoliticaServiceVehiculoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 3);

    private final PoliticaServiceVehiculo politica = new PoliticaServiceVehiculo(12, 10000);

    @Test
    @DisplayName("vence por tiempo justo al cumplirse los 12 meses")
    void vencePorTiempoAlCumplirseElPlazo() {
        Vehiculo vehiculo = Vehiculo.builder().fechaUltimoService(HOY.minusMonths(12)).build();

        assertThat(politica.evaluar(vehiculo, HOY)).contains(MotivoAlerta.TIEMPO);
    }

    @Test
    @DisplayName("no vence un dia antes de cumplirse los 12 meses")
    void noVenceAntesDelPlazo() {
        Vehiculo vehiculo = Vehiculo.builder().fechaUltimoService(HOY.minusMonths(12).plusDays(1)).build();

        assertThat(politica.evaluar(vehiculo, HOY)).isEmpty();
    }

    @Test
    @DisplayName("sin service registrado cuenta desde la fecha de alta")
    void cuentaDesdeLaFechaDeAlta() {
        Vehiculo vehiculo = Vehiculo.builder().fechaAlta(HOY.minusMonths(13)).build();

        assertThat(politica.evaluar(vehiculo, HOY)).contains(MotivoAlerta.TIEMPO);
    }

    @Test
    @DisplayName("vence por kilometraje al recorrer 10.000 km desde el ultimo service")
    void vencePorKilometraje() {
        Vehiculo vehiculo = Vehiculo.builder()
                .fechaUltimoService(HOY.minusMonths(2))
                .kmUltimoService(30000)
                .kilometrajeActual(40000)
                .build();

        assertThat(politica.evaluar(vehiculo, HOY)).contains(MotivoAlerta.KILOMETRAJE);
    }

    @Test
    @DisplayName("sin service registrado cuenta el kilometraje desde el km de alta")
    void cuentaKilometrajeDesdeElAlta() {
        Vehiculo vehiculo = Vehiculo.builder()
                .fechaAlta(HOY.minusMonths(2))
                .kilometrajeAlta(80000)
                .kilometrajeActual(90000)
                .build();

        assertThat(politica.evaluar(vehiculo, HOY)).contains(MotivoAlerta.KILOMETRAJE);
    }

    @Test
    @DisplayName("no evalua kilometraje si no hay km de referencia")
    void sinKmDeReferenciaNoEvaluaKilometraje() {
        Vehiculo vehiculo = Vehiculo.builder()
                .fechaUltimoService(HOY.minusMonths(2))
                .kilometrajeActual(90000)
                .build();

        assertThat(politica.evaluar(vehiculo, HOY)).isEmpty();
    }
}
