package com.taller.gestion_taller.infrastructure.rest.validation.rol;

import com.taller.gestion_taller.domain.model.Rol;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RolValidator implements ConstraintValidator<RolValido, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            Rol.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
