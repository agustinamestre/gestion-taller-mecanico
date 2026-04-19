package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;

import java.util.Arrays;

public enum TipoProducto {
    REPUESTO,
    INSUMO,
    SERVICIO;

    public static TipoProducto from(String value) {
        return Arrays.stream(TipoProducto.values())
                .filter(t -> t.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BusinessRunTimeException(BusinessErrors.campoInvalido("tipo", "debe ser REPUESTO, INSUMO o SERVICIO")));
    }
}
