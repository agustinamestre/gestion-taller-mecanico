package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Builder.Default
    private List<ItemOrdenTrabajo> items = new ArrayList<>();


    public void cambiarEstado(EstadoOrdenTrabajo nuevoEstado) {
        if (!this.estado.puedeTransicionarA(nuevoEstado)) {
            throw new BusinessRunTimeException(
                    BusinessErrors.transicionEstadoInvalidaOrden(this.estado, nuevoEstado));
        }
        if (nuevoEstado == EstadoOrdenTrabajo.FINALIZADO) {
            this.fechaEgreso = LocalDate.now();
        }
        this.estado = nuevoEstado;
    }

    public void modificar(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public void agregarItem(Producto producto, String descripcion,
                            Integer cantidad, BigDecimal precioUnitario) {
        ItemOrdenTrabajo item = ItemOrdenTrabajo.builder()
                .ordenId(this.id)
                .producto(producto)
                .tipo(producto != null ? producto.getTipo() : null)
                .descripcion(descripcion)
                .cantidad(cantidad)
                .precioUnitario(precioUnitario)
                .facturado(false)
                .build();
        this.items.add(item);
    }

    public void modificarItem(Long itemId, Producto producto, String descripcion,
                              Integer cantidad, BigDecimal precioUnitario) {
        ItemOrdenTrabajo item = this.items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.itemOrdenNoEncontrado(itemId)));

        item.modificar(producto, descripcion, cantidad, precioUnitario);
    }

    public void eliminarItem(Long itemId) {
        boolean eliminado = this.items.removeIf(i -> i.getId().equals(itemId));
        if (!eliminado) {
            throw new NotFoundException(BusinessErrors.itemOrdenNoEncontrado(itemId));
        }
    }

    public BigDecimal calcularTotal() {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        return items.stream()
                .map(ItemOrdenTrabajo::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}