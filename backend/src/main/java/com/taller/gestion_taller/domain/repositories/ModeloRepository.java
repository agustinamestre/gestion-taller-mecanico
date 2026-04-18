package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Modelo;
import java.util.List;
import java.util.Optional;

public interface ModeloRepository {
    Modelo save(Modelo modelo);
    List<Modelo> findAll();
    List<Modelo> findByMarcaId(Long marcaId);
    Optional<Modelo> findById(Long id);
    Optional<Modelo> findByNombreAndMarcaId(String nombre, Long marcaId);
}
