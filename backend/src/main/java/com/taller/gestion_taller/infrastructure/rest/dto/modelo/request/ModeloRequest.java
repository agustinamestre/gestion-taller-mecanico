package com.taller.gestion_taller.infrastructure.rest.dto.modelo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModeloRequest (
        @NotBlank(message = "El nombre del modelo es obligatorio")
        String nombre,
        @NotNull(message = "La marca es obligatoria")
        Long marcaId
) { }
