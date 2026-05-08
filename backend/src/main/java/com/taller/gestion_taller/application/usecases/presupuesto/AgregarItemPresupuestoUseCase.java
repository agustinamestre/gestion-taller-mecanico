package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class AgregarItemPresupuestoUseCase implements AgregarItemPresupuesto {

    private final PresupuestoRepository presupuestoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Presupuesto agregar(AgregarItemPresupuestoCommand command) {

        Presupuesto presupuesto = presupuestoRepository.findById(command.presupuestoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(command.presupuestoId())));

        Producto producto = productoRepository.findById(command.productoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.productoNoEncontrado(command.productoId())));

        presupuesto.agregarItem(producto, command.descripcion(),
                command.cantidad(), command.precioUnitario());

        return presupuestoRepository.save(presupuesto);
    }

}
