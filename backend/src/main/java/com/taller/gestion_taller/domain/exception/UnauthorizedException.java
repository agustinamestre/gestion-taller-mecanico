package com.taller.gestion_taller.domain.exception;

public class UnauthorizedException extends BusinessRunTimeException {

    public UnauthorizedException(BusinessError businessError) {
        super(businessError);
    }
}