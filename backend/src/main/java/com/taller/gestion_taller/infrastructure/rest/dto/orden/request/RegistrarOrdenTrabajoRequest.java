package com.taller.gestion_taller.infrastructure.rest.dto.orden.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarOrdenTrabajoRequest(
        String patente,
        Long presupuestoId,
        @NotBlank(message = "La descripcion del problema es obligatoria")
        String descripcionProblema,
        @NotNull(message = "El usuario de creacion es obligatorio")
        Long usuarioCreacionId
) {

    @AssertTrue(message = "Debe enviarse 'patente' o 'presupuestoId' (al menos uno)")
    @JsonIgnore
    public boolean isIdentificacionVehiculoValida() {
        boolean tienePatente = patente != null && !patente.isBlank();
        boolean tienePresupuesto = presupuestoId != null;
        return tienePatente || tienePresupuesto;
    }
}
