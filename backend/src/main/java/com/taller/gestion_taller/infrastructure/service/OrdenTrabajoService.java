package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.orden.*;
import com.taller.gestion_taller.application.usecases.orden.*;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {

    private final RegistrarOrdenTrabajo registrarOrdenTrabajoUseCase;
    private final ObtenerOrdenes obtenerOrdenesUseCase;
    private final CambiarEstadoOrdenTrabajo cambiarEstadoOrdenTrabajoUseCase;
    private final ModificarOrdenTrabajo modificarOrdenTrabajoUseCase;
    private final AgregarItemOrdenTrabajo agregarItemOrdenTrabajoUseCase;
    private final ModificarItemOrdenTrabajo modificarItemOrdenTrabajoUseCase;
    private final EliminarItemOrdenTrabajo eliminarItemOrdenTrabajoUseCase;

    @Transactional
    public OrdenTrabajo registrarOrden(RegistrarOrdenTrabajoCommand command) {
        return registrarOrdenTrabajoUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> obtenerOrdenes(String patente, EstadoOrdenTrabajo estado) {
        return obtenerOrdenesUseCase.obtener(patente, estado);
    }

    @Transactional
    public void cambiarEstado(CambiarEstadoOrdenTrabajoCommand command) {
        cambiarEstadoOrdenTrabajoUseCase.cambiar(command);
    }

    @Transactional
    public OrdenTrabajo modificarOrden(ModificarOrdenTrabajoCommand command) {
        return modificarOrdenTrabajoUseCase.modificar(command);
    }

    @Transactional
    public OrdenTrabajo agregarItem(AgregarItemOrdenTrabajoCommand command) {
        return agregarItemOrdenTrabajoUseCase.agregar(command);
    }

    @Transactional
    public OrdenTrabajo modificarItem(ModificarItemOrdenTrabajoCommand command) {
        return modificarItemOrdenTrabajoUseCase.modificar(command);
    }

    @Transactional
    public void eliminarItem(EliminarItemOrdenTrabajoCommand command) {
        eliminarItemOrdenTrabajoUseCase.eliminar(command);
    }
}