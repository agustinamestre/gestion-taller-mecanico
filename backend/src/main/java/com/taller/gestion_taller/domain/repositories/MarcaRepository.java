package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Marca;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository {
    Marca save(Marca marca);
    List<Marca> findAll();
    Optional<Marca> findByNombre(String nombre);
    Optional<Marca> findById(Long id);
    void eliminar(Long id);
}
