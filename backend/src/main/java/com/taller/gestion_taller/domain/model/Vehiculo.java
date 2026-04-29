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
    private Integer kilometrajeActual;
    @Builder.Default
    private boolean activo = true;


    public Vehiculo actualizarKilometraje(Integer nuevoKilometraje) {
        if (nuevoKilometraje < this.kilometrajeActual) {
            throw new BusinessRunTimeException(BusinessErrors.campoInvalido("kilometrajeActual", "El nuevo kilometraje no puede ser menor al actual"));
        }
        return this.toBuilder()
                .kilometrajeActual(nuevoKilometraje)
                .build();
    }
    
    public Vehiculo actualizarDatos(Modelo nuevoModelo, Integer nuevoAnio, Cliente nuevoCliente) {
        return this.toBuilder()
                .modelo(nuevoModelo)
                .anio(nuevoAnio)
                .cliente(nuevoCliente)
                .build();
    }

    public Vehiculo actualizarFechaUltimoService(LocalDate nuevaFecha) {
        if (nuevaFecha == null || nuevaFecha.isAfter(LocalDate.now())) {
            throw new BusinessRunTimeException(BusinessErrors.campoInvalido("fechaUltimoService", "La fecha del último servicio no puede ser nula o futura"));
        }
        return this.toBuilder()
                .fechaUltimoService(nuevaFecha)
                .build();
    }

    public Vehiculo desactivar() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoYaDesactivado());
        }
        return this.toBuilder()
                .activo(false)
                .build();
    }
}
