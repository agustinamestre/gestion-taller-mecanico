package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ListarModelosUseCase implements ListarModelos {

    private final ModeloRepository modeloRepository;

    @Override
    public List<Modelo> listar(Optional<Long> marcaId) {
        return marcaId
                .map(modeloRepository::findByMarcaId)
                .orElseGet(modeloRepository::findAll);
    }
}
