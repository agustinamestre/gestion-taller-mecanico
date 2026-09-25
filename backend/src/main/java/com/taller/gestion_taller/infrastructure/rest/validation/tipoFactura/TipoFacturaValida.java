package com.taller.gestion_taller.infrastructure.rest.validation.tipoFactura;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TipoFacturaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TipoFacturaValida {
    String message() default "Valores aceptados: [SENIA, FINAL]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
