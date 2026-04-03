package com.taller.gestion_taller.domain.exception;

public class NotFoundException extends BusinessRunTimeException {

    public NotFoundException(BusinessError businessError) {
        super(businessError);
    }

}
