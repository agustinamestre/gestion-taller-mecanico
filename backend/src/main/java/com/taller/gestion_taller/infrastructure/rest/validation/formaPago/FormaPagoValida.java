package com.taller.gestion_taller.infrastructure.rest.validation.formaPago;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FormaPagoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormaPagoValida {
    String message() default "Valores aceptados: [EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA_BANCARIA]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}