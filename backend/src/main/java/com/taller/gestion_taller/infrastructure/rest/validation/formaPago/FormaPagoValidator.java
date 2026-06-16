package com.taller.gestion_taller.infrastructure.rest.validation.formaPago;

import com.taller.gestion_taller.domain.model.FormaPago;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FormaPagoValidator implements ConstraintValidator<FormaPagoValida, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            FormaPago.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
