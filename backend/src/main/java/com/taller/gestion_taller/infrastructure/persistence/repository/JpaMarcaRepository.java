package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.MarcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMarcaRepository extends JpaRepository<MarcaEntity, Long> {
    Optional<MarcaEntity> findByNombre(String nombre);
    Optional<MarcaEntity> findById(Long id);
}
