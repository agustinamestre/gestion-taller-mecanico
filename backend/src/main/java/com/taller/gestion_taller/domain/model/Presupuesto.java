package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static Presupuesto crearNuevo(Vehiculo vehiculo, String observaciones) {
        LocalDate hoy = LocalDate.now();
        return Presupuesto.builder()
                .vehiculo(vehiculo)
                .fechaEmision(hoy)
                .fechaVencimiento(hoy.plusDays(DIAS_VENCIMIENTO_DEFAULT))
                .estado(EstadoPresupuesto.PENDIENTE)
                .observaciones(observaciones)
                .items(new ArrayList<>())
                .build();
    }

    public void agregarItem(TipoProducto tipo, Producto producto,
                            String descripcion, Integer cantidad, BigDecimal precioUnitario) {

        if (this.estado != EstadoPresupuesto.PENDIENTE) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoNoPendiente());
        }

        ItemPresupuesto item = ItemPresupuesto.crearNuevo(
                this.id, tipo, producto, descripcion, cantidad, precioUnitario
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
}
