package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    private String descripcion;
    
    @NotBlank(message = "El tipo de producto es obligatorio")
    private String tipo;
    
    private BigDecimal precioActual;

    private Integer stockActual;
}
