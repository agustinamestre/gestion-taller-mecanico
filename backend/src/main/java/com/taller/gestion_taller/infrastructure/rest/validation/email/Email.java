package com.taller.gestion_taller.infrastructure.rest.validation.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "Email invalido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
