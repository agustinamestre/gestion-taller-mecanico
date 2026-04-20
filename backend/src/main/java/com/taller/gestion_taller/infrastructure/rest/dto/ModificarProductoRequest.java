package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ModificarProductoRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,
        String descripcion,
        @NotBlank(message = "El tipo no puede estar vacío")
        @Pattern(regexp = "REPUESTO|INSUMO|SERVICIO",
                message = "El tipo debe ser REPUESTO, INSUMO o SERVICIO")
        String tipo
) { }
