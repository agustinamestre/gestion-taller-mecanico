package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VehiculoValidator {
    private final VehiculoRepository vehiculoRepository;

    public void validarPatenteUnica(String patente) {
        if (vehiculoRepository.findByPatente(patente).isPresent()) {
            throw new BusinessRunTimeException(BusinessErrors.vehiculoPatenteDuplicada());
        }
    }
}
