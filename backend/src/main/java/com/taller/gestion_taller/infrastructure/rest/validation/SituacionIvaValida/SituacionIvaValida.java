package com.taller.gestion_taller.infrastructure.rest.validation.SituacionIvaValida;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SituacionIvaValidaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SituacionIvaValida {
    String message() default "Situación IVA inválida. Valores aceptados: [RESPONSABLE_INSCRIPTO, MONOTRIBUTISTA, CONSUMIDOR_FINAL, EXENTO]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}