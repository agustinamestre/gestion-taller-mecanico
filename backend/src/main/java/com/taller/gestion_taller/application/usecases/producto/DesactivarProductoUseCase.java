package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesactivarProductoUseCase implements DesactivarProducto {

    private final ProductoRepository productoRepository;

    @Override
    public void desactivar(Long id) {
        productoRepository.findById(id)
                .map(Producto::desactivar)
                .map(productoRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.productoNoEncontrado(id)));
    }
}
