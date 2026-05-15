package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ObtenerOrdenesUseCase implements ObtenerOrdenes {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public List<OrdenTrabajo> obtener(String patente, EstadoOrdenTrabajo estado) {
        if (patente != null) {
            vehiculoRepository.findByPatente(patente)
                    .orElseThrow(() -> new NotFoundException(
                            BusinessErrors.vehiculoNoEncontrado(patente)));
        }

        return ordenTrabajoRepository.findByFiltros(patente, estado);
    }
}