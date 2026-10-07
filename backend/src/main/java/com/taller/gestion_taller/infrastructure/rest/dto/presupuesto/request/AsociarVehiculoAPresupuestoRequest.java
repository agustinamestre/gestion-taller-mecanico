package com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request;

import com.taller.gestion_taller.domain.model.SituacionIva;
import com.taller.gestion_taller.infrastructure.rest.validation.dni.DniValido;
import com.taller.gestion_taller.infrastructure.rest.validation.email.Email;
import com.taller.gestion_taller.infrastructure.rest.validation.telefono.TelefonoValido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AsociarVehiculoAPresupuestoRequest(
        Long vehiculoId,
        @Valid DatosVehiculoNuevo datosVehiculoNuevo,
        Long clienteId,
        @Valid DatosClienteNuevo datosClienteNuevo
) {
    public record DatosVehiculoNuevo(
            @NotBlank(message = "La patente es obligatoria")
            String patente,
            @NotNull(message = "El modelo es obligatorio")
            Long modeloId,
            @NotNull(message = "El año es obligatorio")
            Integer anio,
            @NotNull(message = "El kilometraje es obligatorio")
            @Min(value = 0, message = "El kilometraje debe ser mayor o igual a 0")
            Integer kilometrajeActual
    ) {}

    public record DatosClienteNuevo(
            @NotBlank(message = "El numero de DNI es obligatorio.")
            @DniValido
            String dni,
            @NotBlank(message = "El nombre es obligatorio.")
            String nombre,
            @NotBlank(message = "El apellido es obligatorio.")
            String apellido,
            @TelefonoValido
            String telefono,
            @Email
            String email,
            String direccion,
            @NotNull(message = "La situación IVA es obligatoria.")
            SituacionIva situacionIva
    ) {}
}
