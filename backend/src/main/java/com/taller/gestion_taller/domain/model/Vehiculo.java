package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Vehiculo {
    private Long id;
    private String patente;
    private Modelo modelo;
    private Integer anio;
    private Cliente cliente;
    private LocalDate fechaUltimoService;
    private Integer kmUltimoService;
    private Integer kilometrajeActual;
    private LocalDate fechaAlta;
    private Integer kilometrajeAlta;
    @Builder.Default
    private boolean activo = true;


    public Vehiculo actualizarKilometraje(Integer nuevoKilometraje) {
        if (nuevoKilometraje < this.kilometrajeActual) {
            throw new BusinessRunTimeException(BusinessErrors.campoInvalido("kilometrajeActual", "El nuevo kilometraje no puede ser menor al actual"));
        }

        Integer kmAlta = kmReferenciaService() == null ? this.kilometrajeActual : this.kilometrajeAlta;
        return this.toBuilder()
                .kilometrajeActual(nuevoKilometraje)
                .kilometrajeAlta(kmAlta)
                .build();
    }

    public Vehiculo actualizarDatos(Modelo nuevoModelo, Integer nuevoAnio, Cliente nuevoCliente,
                                    LocalDate nuevaFechaUltimoService) {
        if (nuevoCliente == null) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoSinCliente());
        }
        if (nuevoModelo == null) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoSinModelo());
        }
        return this.toBuilder()
                .modelo(nuevoModelo)
                .anio(nuevoAnio)
                .cliente(nuevoCliente)
                .fechaUltimoService(nuevaFechaUltimoService)
                .build();
    }

    public Vehiculo registrarService(LocalDate fecha) {
        return this.toBuilder()
                .fechaUltimoService(fecha)
                .kmUltimoService(this.kilometrajeActual)
                .build();
    }

    public LocalDate fechaReferenciaService() {
        return fechaUltimoService != null ? fechaUltimoService : fechaAlta;
    }

    public Integer kmReferenciaService() {
        return kmUltimoService != null ? kmUltimoService : kilometrajeAlta;
    }

    public Vehiculo desactivar() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoYaDesactivado());
        }
        return this.toBuilder()
                .activo(false)
                .build();
    }

    public Vehiculo reactivar() {
        if (this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoYaActivo());
        }
        return this.toBuilder()
                .activo(true)
                .build();
    }

    public static Vehiculo crearNuevo(String patente,
                                      Modelo modelo,
                                      Integer anio,
                                      Cliente cliente,
                                      Integer kilometrajeActual) {
        if (patente == null || patente.isBlank()) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoSinPatente());
        }
        if (modelo == null) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoSinModelo());
        }
        if (cliente == null) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoSinCliente());
        }
        return Vehiculo.builder()
                .patente(patente)
                .modelo(modelo)
                .anio(anio)
                .cliente(cliente)
                .kilometrajeActual(kilometrajeActual)
                .fechaAlta(LocalDate.now())
                .kilometrajeAlta(kilometrajeActual)
                .activo(true)
                .build();
    }
}
