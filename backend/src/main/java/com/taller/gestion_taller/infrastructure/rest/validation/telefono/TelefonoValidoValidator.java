package com.taller.gestion_taller.infrastructure.rest.validation.telefono;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class TelefonoValidoValidator implements ConstraintValidator<TelefonoValido, String> {

    @Override
    public boolean isValid(String telefono, ConstraintValidatorContext context) {

        if (isBlank(telefono)) {
            setMessage(context, "El telefono es obligatorio.");
            return false;
        }

        if (!isNumeric(telefono)) {
            setMessage(context, "El telefono solo debe contener numeros.");
            return false;
        }

        if (telefono.charAt(0) == '0') {
            setMessage(context, "El numero de telefono no debe comenzar con 0.");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
