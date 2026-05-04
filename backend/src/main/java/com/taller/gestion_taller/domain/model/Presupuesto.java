package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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

    public static Presupuesto crearNuevo(Vehiculo vehiculo, String observaciones) {
        LocalDate hoy = LocalDate.now();
        return Presupuesto.builder()
                .vehiculo(vehiculo)
                .fechaEmision(hoy)
                .fechaVencimiento(hoy.plusDays(DIAS_VENCIMIENTO_DEFAULT))
                .estado(EstadoPresupuesto.PENDIENTE)
                .observaciones(observaciones)
                .build();
    }
}
