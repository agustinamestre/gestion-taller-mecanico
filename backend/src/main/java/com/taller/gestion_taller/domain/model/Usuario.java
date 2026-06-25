package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private Rol rol;
    @Builder.Default
    private boolean activo = true;

    public void requerirActivo() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.usuarioInactivo(this.username));
        }
    }
}