package com.taller.gestion_taller.infrastructure.rest.validation.rol;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RolValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolValido {
    String message() default "Rol inválido. Valores aceptados: [ADMIN, EMPLEADO]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
