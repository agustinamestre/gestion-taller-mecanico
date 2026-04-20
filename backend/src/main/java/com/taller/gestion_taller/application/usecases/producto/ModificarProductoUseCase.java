package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.ModificarProductoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import com.taller.gestion_taller.domain.service.ProductoValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarProductoUseCase implements ModificarProducto {

    private final ProductoRepository productoRepository;
    private final ProductoValidator productoValidator;

    @Override
    public Producto modificar(Long id, ModificarProductoCommand command) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.productoNoEncontrado(id)));

        TipoProducto nuevoTipo = TipoProducto.from(command.tipo());

        productoValidator.validarUnicidad(
                command.nombre(), nuevoTipo,
                productoExistente.getNombre(), productoExistente.getTipo()
        );

        Producto actualizado = productoExistente.actualizar(command.nombre(), command.descripcion(), nuevoTipo);
        return productoRepository.save(actualizado);
    }
}
