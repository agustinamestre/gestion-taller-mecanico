package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarMarcasUseCase implements ListarMarcas {

    private final MarcaRepository marcaRepository;

    @Override
    public List<Marca> listarMarcas() {
        return marcaRepository.findAll();
    }
}
