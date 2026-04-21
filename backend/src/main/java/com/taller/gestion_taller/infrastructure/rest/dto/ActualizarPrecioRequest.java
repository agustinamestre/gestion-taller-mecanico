package com.taller.gestion_taller.infrastructure.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarPrecioRequest {

    @NotNull(message = "El precio es obligatorio")
    private BigDecimal nuevoPrecio;
}
