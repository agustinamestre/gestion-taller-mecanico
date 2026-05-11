package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.presupuesto.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.EliminarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.ModificarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.application.usecases.presupuesto.*;
import com.taller.gestion_taller.domain.model.Presupuesto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresupuestoService {

    private final RegistrarPresupuesto registrarPresupuestoUseCase;
    private final AgregarItemPresupuesto agregarItemPresupuestoUseCase;
    private final ModificarItemPresupuesto modificarItemPresupuestoUseCase;
    private final ObtenerPresupuesto obtenerPresupuestoUseCase;
    private final ObtenerPresupuestosPorPatente obtenerPresupuestosPorPatenteUseCase;
    private final EliminarItemPresupuesto eliminarItemPresupuestoUseCase;

    @Transactional
    public Presupuesto registrarPresupuesto(RegistrarPresupuestoCommand command) {
        return registrarPresupuestoUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public Presupuesto obtenerPresupuesto(Long id) {
        return obtenerPresupuestoUseCase.obtener(id);
    }

    @Transactional(readOnly = true)
    public List<Presupuesto> obtenerPresupuestosPorPatente(String patente) {
        return obtenerPresupuestosPorPatenteUseCase.obtener(patente);
    }

    @Transactional
    public Presupuesto agregarItem(AgregarItemPresupuestoCommand command) {
        return agregarItemPresupuestoUseCase.agregar(command);
    }

    @Transactional
    public Presupuesto modificarItem(ModificarItemPresupuestoCommand command) {
        return modificarItemPresupuestoUseCase.modificar(command);
    }

    @Transactional
    public void eliminarItem(EliminarItemPresupuestoCommand command) {
        eliminarItemPresupuestoUseCase.eliminar(command);
    }
}
