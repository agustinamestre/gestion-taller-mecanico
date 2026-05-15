package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
}