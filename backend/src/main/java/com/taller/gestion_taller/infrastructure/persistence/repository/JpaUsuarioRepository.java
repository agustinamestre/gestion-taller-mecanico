package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsername(String username);
}