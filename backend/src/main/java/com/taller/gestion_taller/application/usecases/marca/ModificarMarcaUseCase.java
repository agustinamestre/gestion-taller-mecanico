package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.application.command.ModificarMarcaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.service.MarcaValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarMarcaUseCase implements ModificarMarca {

    private final MarcaRepository marcaRepository;
    private final MarcaValidator marcaValidator;


    @Override
    public Marca modificarMarca(Long id, ModificarMarcaCommand command) {
        return marcaRepository.findById(id)
                .map(marca -> {
                    marcaValidator.validarNombreUnico(command.nombre(), marca.getNombre());
                    return marca;
                })
                .map(marca -> marca.actualizarNombre(command.nombre()))
                .map(marcaRepository::save)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.marcaNoEncontrada()));
    }
}
