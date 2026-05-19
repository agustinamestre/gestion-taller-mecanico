package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.AgregarItemOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgregarItemOrdenTrabajoUseCase implements AgregarItemOrdenTrabajo {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public OrdenTrabajo agregar(AgregarItemOrdenTrabajoCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.ordenId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.ordenId())));

        Producto producto = null;
        if (command.productoId() != null) {
            producto = productoRepository.findById(command.productoId())
                    .orElseThrow(() -> new NotFoundException(
                            BusinessErrors.productoNoEncontrado(command.productoId())));
        }

        orden.agregarItem(producto, command.descripcion(), command.cantidad(), command.precioUnitario());
        return ordenTrabajoRepository.save(orden);
    }
}
