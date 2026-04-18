package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.ModeloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JpaModeloRepository extends JpaRepository<ModeloEntity, Long> {
    List<ModeloEntity> findByMarcaId(Long marcaId);
    Optional<ModeloEntity> findByNombreAndMarcaId(String nombre, Long marcaId);
}
