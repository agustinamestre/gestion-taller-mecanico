package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record MarcaRequest (
        @NotBlank(message = "El nombre de la marca es obligatorio")
        String nombre
) { }



