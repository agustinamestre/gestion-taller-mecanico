package com.taller.gestion_taller.infrastructure.rest.validation.telefono;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TelefonoValidoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TelefonoValido {
    String message() default "Numero de telefono invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
