package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.ModificarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarItemPresupuestoUseCase implements ModificarItemPresupuesto {

    private final PresupuestoRepository presupuestoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Presupuesto modificar(ModificarItemPresupuestoCommand command) {

        Presupuesto presupuesto = presupuestoRepository.findById(command.presupuestoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(command.presupuestoId())));

        Producto producto = resolverProducto(command);

        presupuesto.modificarItem(
                command.itemId(),
                producto,
                command.descripcion(),
                command.cantidad(),
                command.precioUnitario()
        );

        return presupuestoRepository.save(presupuesto);
    }

    private Producto resolverProducto(ModificarItemPresupuestoCommand command) {
        if (command.productoId() == null) {
            return null;
        }
        return productoRepository.findById(command.productoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.productoNoEncontrado(command.productoId())));
    }
}
