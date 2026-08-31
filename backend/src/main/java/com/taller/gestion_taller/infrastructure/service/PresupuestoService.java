package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.presupuesto.*;
import com.taller.gestion_taller.application.usecases.presupuesto.*;
import com.taller.gestion_taller.domain.model.Presupuesto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PresupuestoService {

    private final RegistrarPresupuesto registrarPresupuestoUseCase;
    private final AgregarItemPresupuesto agregarItemPresupuestoUseCase;
    private final ModificarItemPresupuesto modificarItemPresupuestoUseCase;
    private final ObtenerPresupuesto obtenerPresupuestoUseCase;
    private final ListarPresupuestos listarPresupuestosUseCase;
    private final EliminarItemPresupuesto eliminarItemPresupuestoUseCase;
    private final CambiarEstadoPresupuesto cambiarEstadoPresupuestoUseCase;
    private final AsociarVehiculoAPresupuesto asociarVehiculoAPresupuestoUseCase;
    private final MarcarPresupuestosVencidos marcarPresupuestosVencidosUseCase;
    private final EliminarPresupuestosVencidosYRechazados eliminarPresupuestosVencidosYRechazadosUseCase;

    @Transactional
    public Presupuesto registrarPresupuesto(RegistrarPresupuestoCommand command) {
        return registrarPresupuestoUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public Presupuesto obtenerPresupuesto(Long id) {
        return obtenerPresupuestoUseCase.obtener(id);
    }

    @Transactional(readOnly = true)
    public List<Presupuesto> listarPresupuestos(String patente, LocalDate fechaDesde, LocalDate fechaHasta) {
        return listarPresupuestosUseCase.listar(patente, fechaDesde, fechaHasta);
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

    @Transactional
    public void cambiarEstado(CambiarEstadoPresupuestoCommand command) {
        cambiarEstadoPresupuestoUseCase.cambiar(command);
    }

    @Transactional
    public Presupuesto asociarVehiculo(AsociarVehiculoAPresupuestoCommand command) {
        return asociarVehiculoAPresupuestoUseCase.asociar(command);
    }

    @Transactional
    public int marcarPresupuestosVencidos() {
        return marcarPresupuestosVencidosUseCase.marcar();
    }

    @Transactional
    public int eliminarPresupuestosVencidosYRechazados() {
        return eliminarPresupuestosVencidosYRechazadosUseCase.eliminar();
    }
}
