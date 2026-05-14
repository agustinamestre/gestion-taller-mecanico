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
import java.util.Collections;
import java.util.List;

@Getter @Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Presupuesto {

    private static final int DIAS_VENCIMIENTO_DEFAULT = 30;

    private Long id;
    private Vehiculo vehiculo;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private EstadoPresupuesto estado;
    private String observaciones;
    @Builder.Default
    private List<ItemPresupuesto> items = new ArrayList<>();

    public List<ItemPresupuesto> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void agregarItem(Producto producto,
                            String descripcion, Integer cantidad, BigDecimal precioUnitario) {

        if (this.estado != EstadoPresupuesto.PENDIENTE) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoNoPendiente());
        }

        ItemPresupuesto item = ItemPresupuesto.crearNuevo(
                this.id, producto, descripcion, cantidad, precioUnitario
        );
        this.items.add(item);
    }

    public BigDecimal calcularTotal() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(ItemPresupuesto::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void modificarItem(Long itemId, Producto producto,
                              String descripcion, Integer cantidad,
                              BigDecimal precioUnitario) {
        if (this.estado != EstadoPresupuesto.PENDIENTE) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoNoPendiente());
        }

        ItemPresupuesto item = this.items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.itemNoEncontrado(itemId)));

        item.modificar(producto, descripcion, cantidad, precioUnitario);
    }

    public void eliminarItem(Long itemId) {
        if (this.estado != EstadoPresupuesto.PENDIENTE) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoNoPendiente());
        }

        boolean eliminado = this.items.removeIf(i -> i.getId().equals(itemId));

        if (!eliminado) {
            throw new NotFoundException(BusinessErrors.itemNoEncontrado(itemId));
        }
    }

    public void cambiarEstado(EstadoPresupuesto nuevoEstado) {
        if (!this.estado.puedeTransicionarA(nuevoEstado)) {
            throw new BusinessRunTimeException(
                    BusinessErrors.transicionEstadoInvalida(this.estado, nuevoEstado));
        }
        this.estado = nuevoEstado;
    }
}
