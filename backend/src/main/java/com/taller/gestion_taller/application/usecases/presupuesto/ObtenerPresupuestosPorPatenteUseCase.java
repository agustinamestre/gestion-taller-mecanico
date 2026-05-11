package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ObtenerPresupuestosPorPatenteUseCase implements ObtenerPresupuestosPorPatente{

    private final PresupuestoRepository presupuestoRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public List<Presupuesto> obtener(String patente) {
        vehiculoRepository.findByPatente(patente)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado(patente)));

        return presupuestoRepository.findByPatente(patente);
    }

}
