package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemOrdenTrabajo {

    private Long id;
    private Long ordenId;
    private Producto producto;
    private TipoProducto tipo;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private Boolean facturado;

    public BigDecimal calcularSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public void modificar(Producto producto, String descripcion,
                          Integer cantidad, BigDecimal precioUnitario) {
        if (producto != null) {
            this.producto = producto;
            this.tipo = producto.getTipo();
        }
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
}