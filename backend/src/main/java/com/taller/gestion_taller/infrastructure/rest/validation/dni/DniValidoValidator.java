package com.taller.gestion_taller.infrastructure.rest.validation.dni;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class DniValidoValidator implements ConstraintValidator<DniValido, String> {
    @Override
    public boolean isValid(String nroTributario, ConstraintValidatorContext context) {

        if(isBlank(nroTributario)){
            setMessage(context, "El numero de DNI es obligatorio.");
            return false;
        }

        if (!isNumeric(nroTributario)) {
            setMessage(context, "Numero de DNI invalido.");
            return false;
        }

        if(nroTributario.length() < 7 || nroTributario.length() > 8){
            setMessage(context, "El numero de DNI debe tener entre 7 u 8 digitos.");
            return false;
        }
        
        if(nroTributario.charAt(0) == '0'){
            setMessage(context, "El numero de DNI no puede empezar con 0.");
            return false;
        }
        
        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}