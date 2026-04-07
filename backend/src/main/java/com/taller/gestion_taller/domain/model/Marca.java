package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Marca {
    private Long id;
    private String nombre;
    @Builder.Default
    private boolean activo = true;

    public Marca actualizarNombre(String nuevoNombre) {
        return this.toBuilder()
                .nombre(nuevoNombre)
                .build();
    }

    public Marca desactivar() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.marcaYaDesactivada());
        }
        return this.toBuilder()
                .activo(false)
                .build();
    }
}
