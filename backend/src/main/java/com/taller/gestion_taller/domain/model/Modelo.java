package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Modelo {
    private Long id;
    private String nombre;
    private Marca marca;

    public Modelo actualizar(String nuevoNombre, Marca nuevaMarca) {
        return this.toBuilder()
                .nombre(nuevoNombre)
                .marca(nuevaMarca)
                .build();
    }

}
