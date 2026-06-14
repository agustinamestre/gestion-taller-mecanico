package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaJpaRepository extends JpaRepository<FacturaEntity, Long> {

    @Query("""
        SELECT f FROM FacturaEntity f
        JOIN FETCH f.ordenTrabajo ot
        JOIN FETCH ot.vehiculo v
        JOIN FETCH v.cliente c
        JOIN FETCH ot.items
        WHERE f.ordenTrabajo.id = :ordenTrabajoId
    """)
    Optional<FacturaEntity> findByOrdenTrabajoId(@Param("ordenTrabajoId") Long ordenTrabajoId);

    @Query("""
        SELECT f FROM FacturaEntity f
        JOIN FETCH f.ordenTrabajo ot
        JOIN FETCH ot.vehiculo v
        JOIN FETCH v.cliente c
        JOIN FETCH ot.items
        WHERE f.id = :id
    """)
    Optional<FacturaEntity> findById(@Param("id") Long id);
}