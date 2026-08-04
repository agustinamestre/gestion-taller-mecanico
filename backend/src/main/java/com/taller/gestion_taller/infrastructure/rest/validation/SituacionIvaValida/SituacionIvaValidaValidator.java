package com.taller.gestion_taller.infrastructure.rest.validation.SituacionIvaValida;

import com.taller.gestion_taller.domain.model.SituacionIva;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SituacionIvaValidaValidator implements ConstraintValidator<SituacionIvaValida, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            SituacionIva.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
