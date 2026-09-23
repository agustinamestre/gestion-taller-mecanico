package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Presupuesto {

    private static final int DIAS_VENCIMIENTO_DEFAULT = 30;
    private static final List<EstadoPresupuesto> ESTADOS_VENCIBLES =
            List.of(EstadoPresupuesto.PENDIENTE, EstadoPresupuesto.APROBADO);

    private Long id;
    private Vehiculo vehiculo;
    private String dni;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private EstadoPresupuesto estado;
    private String observaciones;
    @Builder.Default
    private List<ItemPresupuesto> items = new ArrayList<>();

    public static Presupuesto crearNuevo(Vehiculo vehiculo, String dni, String observaciones) {
        LocalDate hoy = LocalDate.now();
        return Presupuesto.builder()
                .vehiculo(vehiculo)
                .dni(resolverDni(vehiculo, dni))
                .observaciones(observaciones)
                .estado(EstadoPresupuesto.PENDIENTE)
                .fechaEmision(hoy)
                .fechaVencimiento(hoy.plusDays(DIAS_VENCIMIENTO_DEFAULT))
                .items(new ArrayList<>())
                .build();
    }

    private static String resolverDni(Vehiculo vehiculo, String dni) {
        if (vehiculo == null) {
            if (isBlank(dni)) {
                throw new BusinessRunTimeException(BusinessErrors.presupuestoSinDni());
            }
            return dni;
        }

        String dniCliente = vehiculo.getCliente().getDni();

        if (isNotBlank(dni) && !dni.equalsIgnoreCase(dniCliente.trim())) {
            throw new BusinessRunTimeException(BusinessErrors.dniInconsistenteConVehiculo(dni));
        }

        return dniCliente;
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

    public void marcarComoVencido() {
        if (!ESTADOS_VENCIBLES.contains(this.estado)) {
            throw new BusinessRunTimeException(
                    BusinessErrors.transicionEstadoInvalida(this.estado, EstadoPresupuesto.VENCIDO));
        }
        if (!this.fechaVencimiento.isBefore(LocalDate.now())) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoAunNoVencido());
        }
        this.estado = EstadoPresupuesto.VENCIDO;
    }

    public void marcarComoUtilizado() {
        if (this.estado != EstadoPresupuesto.APROBADO) {
            throw new BusinessRunTimeException(
                    BusinessErrors.transicionEstadoInvalida(this.estado, EstadoPresupuesto.UTILIZADO));
        }
        this.estado = EstadoPresupuesto.UTILIZADO;
    }

    public void cancelar() {
        if (this.estado != EstadoPresupuesto.UTILIZADO) {
            throw new BusinessRunTimeException(
                    BusinessErrors.transicionEstadoInvalida(this.estado, EstadoPresupuesto.CANCELADO));
        }
        this.estado = EstadoPresupuesto.CANCELADO;
    }

    public void asociarVehiculo(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo a asociar no puede ser nulo");

        if (this.estado != EstadoPresupuesto.PENDIENTE) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoNoPendiente());
        }
        if (this.vehiculo != null) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoYaTieneVehiculo());
        }

        String dniCliente = vehiculo.getCliente().getDni();
        if (this.dni != null && !this.dni.equals(dniCliente)) {
            throw new BusinessRunTimeException(BusinessErrors.dniInconsistenteConVehiculo(this.dni));
        }

        this.vehiculo = vehiculo;
        this.dni = dniCliente;
    }

    private void validarInvariantesParaNuevoEstado(EstadoPresupuesto nuevoEstado) {
        if (nuevoEstado == EstadoPresupuesto.APROBADO && this.vehiculo == null) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoSinVehiculoAsociado());
        }
    }
}
