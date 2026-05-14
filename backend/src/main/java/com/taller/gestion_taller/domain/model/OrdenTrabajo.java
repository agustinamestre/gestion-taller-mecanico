package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class OrdenTrabajo {

    private Long id;
    private Vehiculo vehiculo;
    private Presupuesto presupuesto;
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;
    private String descripcionProblema;
    private EstadoOrdenTrabajo estado;
    private Long usuarioCreacionId;
}