package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesactivarModeloUseCase implements DesactivarModelo {

    private final ModeloRepository modeloRepository;

    @Override
    public void desactivar(Long id) {
        modeloRepository.findById(id)
                .map(Modelo::desactivar)
                .map(modeloRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.modeloNoEncontrado()));
    }
}
