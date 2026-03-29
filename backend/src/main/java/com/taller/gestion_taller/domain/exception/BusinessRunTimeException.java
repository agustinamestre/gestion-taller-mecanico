package com.taller.gestion_taller.domain.exception;

import lombok.Getter;

import java.util.List;
import java.util.function.Supplier;

@Getter
public class BusinessRunTimeException extends RuntimeException {

    private final BusinessError businessError;
    private final List<BusinessError> errors;

    public BusinessRunTimeException(BusinessError businessError) {
        super(businessError.message());
        this.businessError = businessError;
        this.errors = List.of(businessError);
    }

    public BusinessRunTimeException(List<BusinessError> errors) {
        super(errors.stream()
                .map(BusinessError::message)
                .reduce((a, b) -> a + "; " + b)
                .orElse("Error de negocio"));
        this.businessError = errors.isEmpty() ? null : errors.getFirst();
        this.errors = List.copyOf(errors);
    }

    public static Supplier<BusinessRunTimeException> of(BusinessError businessError) {
        return () -> new BusinessRunTimeException(businessError);
    }

    public static BusinessRunTimeException of(List<BusinessError> errors) {
        return new BusinessRunTimeException(errors);
    }
}
