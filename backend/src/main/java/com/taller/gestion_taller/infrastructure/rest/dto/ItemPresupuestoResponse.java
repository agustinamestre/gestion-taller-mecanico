package com.taller.gestion_taller.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemPresupuestoResponse {
    private Long id;
    private Long presupuestoId;
    private Long productoId;
    private String tipo;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
