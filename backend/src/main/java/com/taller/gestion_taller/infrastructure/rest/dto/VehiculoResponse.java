package com.taller.gestion_taller.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehiculoResponse {
    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private Integer kilometrajeActual;
    private ClienteSummaryResponse cliente;
    private boolean activo;
}
