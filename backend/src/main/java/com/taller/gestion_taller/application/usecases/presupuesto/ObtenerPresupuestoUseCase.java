package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObtenerPresupuestoUseCase implements ObtenerPresupuesto{

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public Presupuesto obtener(Long id) {
        return presupuestoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(id)));
    }
}
