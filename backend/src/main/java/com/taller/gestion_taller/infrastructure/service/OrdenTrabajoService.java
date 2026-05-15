package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.application.usecases.orden.ObtenerOrdenes;
import com.taller.gestion_taller.application.usecases.orden.RegistrarOrdenTrabajo;
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

    @Transactional
    public OrdenTrabajo registrarOrden(RegistrarOrdenTrabajoCommand command) {
        return registrarOrdenTrabajoUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> obtenerOrdenes(String patente, EstadoOrdenTrabajo estado) {
        return obtenerOrdenesUseCase.obtener(patente, estado);
    }
}