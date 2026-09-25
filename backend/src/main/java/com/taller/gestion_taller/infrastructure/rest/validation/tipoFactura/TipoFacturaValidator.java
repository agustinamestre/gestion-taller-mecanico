package com.taller.gestion_taller.infrastructure.rest.validation.tipoFactura;

import com.taller.gestion_taller.domain.model.TipoFactura;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoFacturaValidator implements ConstraintValidator<TipoFacturaValida, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            TipoFactura.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
