package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ActualizarPrecioProductoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActualizarPrecioProductoUseCase implements ActualizarPrecioProducto {

    private final ProductoRepository productoRepository;

    @Override
    public Producto actualizar(Long id, ActualizarPrecioProductoCommand command) {
        return productoRepository.findById(id)
                .map(producto -> producto.actualizarPrecio(command.getNuevoPrecio()))
                .map(productoRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.productoNoEncontrado(id)));
    }
}
