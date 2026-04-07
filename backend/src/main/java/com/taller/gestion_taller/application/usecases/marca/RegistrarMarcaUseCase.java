package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.application.command.RegistrarMarcaCommand;
import com.taller.gestion_taller.application.mapper.MarcaApplicationMapper;
import com.taller.gestion_taller.domain.service.MarcaValidator;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RegistrarMarcaUseCase implements RegistrarMarca {

    private final MarcaApplicationMapper marcaApplicationMapper;
    private final MarcaValidator marcaValidator;
    private final MarcaRepository marcaRepository;

    @Override
    public Marca registrarMarca(RegistrarMarcaCommand command) {
        return Optional.of(command)
                .map(marcaApplicationMapper::commandToDomain)
                .map(marca -> {
                    marcaValidator.validarNombreUnico(marca.getNombre(), null);
                    return marca;
                })
                .map(marcaRepository::save)
                .orElseThrow();
    }
}
