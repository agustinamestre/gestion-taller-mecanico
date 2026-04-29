package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
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
    @Builder.Default
    private boolean activo = true;

    public Modelo actualizar(String nuevoNombre, Marca nuevaMarca) {
        return this.toBuilder()
                .nombre(nuevoNombre)
                .marca(nuevaMarca)
                .build();
    }

    public Modelo desactivar() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.modeloYaDesactivado());
        }
        return this.toBuilder()
                .activo(false)
                .build();
    }

    public void requerirActivo() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.modeloYaDesactivado());
        }
    }
}
