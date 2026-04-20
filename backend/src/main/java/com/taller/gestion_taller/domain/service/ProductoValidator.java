package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductoValidator {

    private final ProductoRepository productoRepository;

    public void validarUnicidad(String nuevoNombre, TipoProducto nuevoTipo,
                                String nombreActual, TipoProducto tipoActual) {
        if (huboCambioEnIdentidad(nuevoNombre, nuevoTipo, nombreActual, tipoActual)) {
            if (productoRepository.existePorNombreYTipo(nuevoNombre, nuevoTipo)) {
                throw new BusinessRunTimeException(BusinessErrors.productoDuplicado(nuevoNombre, nuevoTipo));
            }
        }
    }

    private boolean huboCambioEnIdentidad(String nuevoNombre, TipoProducto nuevoTipo,
                                          String nombreActual, TipoProducto tipoActual) {
        return !nuevoNombre.equals(nombreActual) || !nuevoTipo.equals(tipoActual);
    }
}
