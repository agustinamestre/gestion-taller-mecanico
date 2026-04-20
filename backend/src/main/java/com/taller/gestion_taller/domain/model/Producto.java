package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
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

    public Producto actualizar(String nombre, String descripcion, TipoProducto tipo) {
        return this.toBuilder()
                .nombre(nombre)
                .descripcion(deberiaMantenerDescripcionAnterior(descripcion) ? this.descripcion : descripcion)
                .tipo(tipo)
                .build();
    }

    public Producto desactivar() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.productoYaDesactivado());
        }
        return this.toBuilder()
                .activo(false)
                .build();
    }

    private boolean deberiaMantenerDescripcionAnterior(String nuevaDescripcion) {
        return nuevaDescripcion == null || nuevaDescripcion.isBlank();
    }

}
