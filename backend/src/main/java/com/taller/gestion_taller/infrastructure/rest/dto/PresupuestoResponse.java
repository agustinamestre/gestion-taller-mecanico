package com.taller.gestion_taller.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PresupuestoResponse {
    private Long id;
    private String patenteVehiculo;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private String estado;
    private String observaciones;
}
