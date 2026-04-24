package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.VehiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaVehiculoRepository extends JpaRepository<VehiculoEntity, Long> {
    Optional<VehiculoEntity> findByPatente(String patente);
    List<VehiculoEntity> findByClienteId(Long clienteId);
}
