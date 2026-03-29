package com.taller.gestion_taller.infrastructure.rest.validation.dni;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DniValidoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DniValido {

    String message() default "Numero de DNI invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}