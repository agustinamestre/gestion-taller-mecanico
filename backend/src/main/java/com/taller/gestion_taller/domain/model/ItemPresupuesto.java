package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemPresupuesto {

    private Long id;
    private Long presupuestoId;
    private Producto producto;
    private TipoProducto tipo;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;

    public static ItemPresupuesto crearNuevo(Long presupuestoId,
                                             Producto producto,
                                             String descripcion,
                                             Integer cantidad,
                                             BigDecimal precioUnitario) {

        return ItemPresupuesto.builder()
                .presupuestoId(presupuestoId)
                .tipo(producto.getTipo())
                .producto(producto)
                .descripcion(descripcion)
                .cantidad(cantidad)
                .precioUnitario(precioUnitario)
                .build();
    }

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
