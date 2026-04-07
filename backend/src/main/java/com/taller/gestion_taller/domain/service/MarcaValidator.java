package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MarcaValidator {
    private final MarcaRepository marcaRepository;

    public void validarNombreUnico(String nuevoNombre, String nombreActual) {
        Optional.ofNullable(nuevoNombre)
                .filter(nombre -> nombreActual == null || !nombre.equalsIgnoreCase(nombreActual))
                .flatMap(marcaRepository::findByNombre)
                .ifPresent(existe -> {
                    throw new BusinessRunTimeException(BusinessErrors.marcaDuplicada(nuevoNombre));
                });
    }
}
