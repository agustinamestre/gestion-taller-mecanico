package com.taller.gestion_taller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Marca {
    private Long id;
    private String nombre;

    public Marca actualizarNombre(String nuevoNombre) {
        return this.toBuilder()
                .nombre(nuevoNombre)
                .build();
    }

}
