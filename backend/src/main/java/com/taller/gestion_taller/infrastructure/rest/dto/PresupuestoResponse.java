package com.taller.gestion_taller.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private List<ItemPresupuestoResponse> items;
    private BigDecimal total;
}
