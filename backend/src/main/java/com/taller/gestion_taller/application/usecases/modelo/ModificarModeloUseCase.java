package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.application.command.ModificarModeloCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.service.ModeloValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModificarModeloUseCase implements ModificarModelo {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloValidator modeloValidator;

    @Override
    public Modelo modificar(Long id, ModificarModeloCommand command) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.modeloNoEncontrado()));

        Marca marca = marcaRepository.findById(command.marcaId())
                .orElseThrow(() -> new NotFoundException(BusinessErrors.marcaNoEncontrada()));

        modeloValidator.validarUnico(command.nombre(), command.marcaId(), id);

        Modelo modeloActualizado = modelo.actualizar(command.nombre(), marca);
        return modeloRepository.save(modeloActualizado);
    }
}
