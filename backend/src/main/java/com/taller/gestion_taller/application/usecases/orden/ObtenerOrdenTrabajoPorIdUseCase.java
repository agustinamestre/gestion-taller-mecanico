package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObtenerOrdenTrabajoPorIdUseCase implements ObtenerOrdenTrabajoPorId {

    private final OrdenTrabajoRepository ordenTrabajoRepository;

    @Override
    public OrdenTrabajo obtener(Long id) {
        return ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.ordenNoEncontrada(id)));
    }
}
