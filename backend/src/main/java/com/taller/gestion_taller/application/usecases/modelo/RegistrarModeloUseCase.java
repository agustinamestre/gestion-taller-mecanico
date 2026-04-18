package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.application.command.RegistrarModeloCommand;
import com.taller.gestion_taller.application.mapper.ModeloApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.service.ModeloValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarModeloUseCase implements RegistrarModelo {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloApplicationMapper modeloApplicationMapper;
    private final ModeloValidator modeloValidator;

    @Override
    public Modelo registrar(RegistrarModeloCommand command) {
        Marca marca = marcaRepository.findById(command.marcaId())
                .orElseThrow(() -> new NotFoundException(BusinessErrors.marcaNoEncontrada()));

        modeloValidator.validarUnico(command.nombre(), command.marcaId(), null);

        Modelo modelo = modeloApplicationMapper.commandToDomain(command, marca);
        return modeloRepository.save(modelo);
    }
}
