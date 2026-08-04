package com.taller.gestion_taller.infrastructure.rest.dto.cliente.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taller.gestion_taller.domain.model.SituacionIva;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.response.VehiculoSummaryResponse;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClienteResponse(
        Long id,
        String dni,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion,
        SituacionIva situacionIva,
        boolean activo,
        LocalDate fechaCreacion,
        LocalDate fechaModificacion,
        List<VehiculoSummaryResponse> vehiculos

) {
}
