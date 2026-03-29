package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Cliente {

    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    @Builder.Default
    private boolean activo = true;
    @Builder.Default
    private LocalDate fechaCreacion = LocalDate.now();
    @Builder.Default
    private LocalDate fechaModificacion = LocalDate.now();

}
