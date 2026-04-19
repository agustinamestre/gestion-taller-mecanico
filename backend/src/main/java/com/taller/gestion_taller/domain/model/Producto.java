package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Producto {
    private Long id;
    private String nombre;
    private String descripcion;
    private TipoProducto tipo;
    private BigDecimal precioActual;
    private Integer stockActual;
    @Builder.Default
    private boolean activo = true;

}
