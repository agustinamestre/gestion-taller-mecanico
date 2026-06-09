package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
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

    /**
     * Punto de entrada oficial para crear un nuevo presupuesto.
     * <p>
     * Soporta el flujo del taller donde un presupuesto puede emitirse "en frío"
     * (sin vehículo registrado) cuando un cliente potencial pide cotización.
     * <p>
     * Estado inicial: {@link EstadoPresupuesto#PENDIENTE}.<br>
     * Fecha emisión: hoy.<br>
     * Fecha vencimiento: hoy + {@value #DIAS_VENCIMIENTO_DEFAULT} días.
     * <p>
     * El vehículo es opcional (R1). Para asociarlo posteriormente usar {@link #asociarVehiculo(Vehiculo)}.
     */
    public static Presupuesto crearNuevo(Vehiculo vehiculo, String observaciones) {
        LocalDate hoy = LocalDate.now();
        return Presupuesto.builder()
                .vehiculo(vehiculo)
                .observaciones(observaciones)
                .estado(EstadoPresupuesto.PENDIENTE)
                .fechaEmision(hoy)
                .fechaVencimiento(hoy.plusDays(DIAS_VENCIMIENTO_DEFAULT))
                .items(new ArrayList<>())
                .build();
    }

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
        validarInvariantesParaNuevoEstado(nuevoEstado);
        this.estado = nuevoEstado;
    }

    public void asociarVehiculo(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo a asociar no puede ser nulo");

        if (this.estado != EstadoPresupuesto.PENDIENTE) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoNoPendiente());
        }
        if (this.vehiculo != null) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoYaTieneVehiculo());
        }

        this.vehiculo = vehiculo;
    }

    private void validarInvariantesParaNuevoEstado(EstadoPresupuesto nuevoEstado) {
        if (nuevoEstado == EstadoPresupuesto.APROBADO && this.vehiculo == null) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoSinVehiculoAsociado());
        }
    }
}
