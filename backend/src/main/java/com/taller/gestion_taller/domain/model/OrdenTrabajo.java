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
import java.util.List;

@Getter
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

    /**
     * Punto de entrada oficial para crear una nueva orden de trabajo.
     * <p>
     * Garantiza la invariante R3: toda orden debe tener un vehículo asociado.
     * El estado inicial es {@link EstadoOrdenTrabajo#INGRESADO} y la fecha de ingreso
     * se setea automáticamente al día actual. El presupuesto es opcional (Flujos 2 y 3 del taller).
     * <p>
     * Las invariantes adicionales (presupuesto APROBADO, coherencia con vehículo, etc.)
     * son responsabilidad del caso de uso que orquesta la creación.
     */
    public static OrdenTrabajo crearNueva(Vehiculo vehiculo,
                                          Presupuesto presupuesto,
                                          String descripcionProblema,
                                          Long usuarioCreacionId) {
        if (vehiculo == null) {
            throw new BusinessRunTimeException(BusinessErrors.ordenSinVehiculo());
        }
        if (descripcionProblema == null || descripcionProblema.isBlank()) {
            throw new BusinessRunTimeException(BusinessErrors.ordenSinDescripcion());
        }
        if (usuarioCreacionId == null) {
            throw new BusinessRunTimeException(BusinessErrors.ordenSinUsuarioCreacion());
        }
        return OrdenTrabajo.builder()
                .vehiculo(vehiculo)
                .presupuesto(presupuesto)
                .descripcionProblema(descripcionProblema)
                .usuarioCreacionId(usuarioCreacionId)
                .estado(EstadoOrdenTrabajo.INGRESADO)
                .fechaIngreso(LocalDate.now())
                .items(new ArrayList<>())
                .build();
    }

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