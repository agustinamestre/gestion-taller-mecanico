package com.taller.gestion_taller.infrastructure.rest.dto.factura.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnularFacturaRequest {

    @NotBlank(message = "El motivo de anulación no puede estar vacío.")
    private String motivo;
}
