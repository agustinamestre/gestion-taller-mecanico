package com.taller.gestion_taller.infrastructure.rest.dto.alerta.request;

import com.taller.gestion_taller.domain.model.MedioContacto;
import jakarta.validation.constraints.NotNull;

public record ContactarAlertaRequest(
        @NotNull(message = "El medio de contacto es obligatorio")
        MedioContacto medioContacto,

        String observaciones
) {}
