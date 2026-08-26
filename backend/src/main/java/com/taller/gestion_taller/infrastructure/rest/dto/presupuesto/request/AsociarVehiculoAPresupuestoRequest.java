package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request;

import com.taller.gestion_taller.domain.model.SituacionIva;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AsociarVehiculoAPresupuestoRequest(
        Long vehiculoId,
        DatosVehiculoNuevo datosVehiculoNuevo,
        Long clienteId,
        DatosClienteNuevo datosClienteNuevo
) {
    public record DatosVehiculoNuevo(
            @NotBlank String patente,
            @NotNull Long modeloId,
            @NotNull Integer anio,
            @NotNull Integer kilometrajeActual
    ) {}

    public record DatosClienteNuevo(
            @NotBlank String dni,
            @NotBlank String nombre,
            @NotBlank String apellido,
            @NotBlank String telefono,
            @Email @NotBlank String email,
            @NotBlank String direccion,
            @NotNull SituacionIva situacionIva
    ) {}
}