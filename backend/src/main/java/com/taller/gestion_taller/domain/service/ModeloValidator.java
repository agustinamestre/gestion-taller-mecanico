package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModeloValidator {
    private final ModeloRepository modeloRepository;

    public void validarUnico(String nombre, Long marcaId, Long idActual) {
        modeloRepository.findByNombreAndMarcaId(nombre, marcaId)
                .filter(m -> idActual == null || !m.getId().equals(idActual))
                .ifPresent(m -> {
                    throw new BusinessRunTimeException(BusinessErrors.modeloDuplicado(nombre, m.getMarca().getNombre()));
                });
    }
}
