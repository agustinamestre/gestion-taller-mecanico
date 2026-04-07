package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesactivarMarcaUseCase implements DesactivarMarca {

    private final MarcaRepository marcaRepository;

    @Override
    public void desactivarMarca(Long id) {
        marcaRepository.findById(id)
                .map(Marca::desactivar)
                .map(marcaRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.marcaNoEncontrada()));
    }
}
