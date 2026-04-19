package com.taller.gestion_taller.infrastructure.rest.dto;

import com.taller.gestion_taller.domain.model.TipoProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private TipoProducto tipo;
    private BigDecimal precioActual;
    private Integer stockActual;
    private boolean activo;
}
