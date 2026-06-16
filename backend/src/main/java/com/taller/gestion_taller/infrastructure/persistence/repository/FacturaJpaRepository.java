package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    @Query("""
    SELECT f FROM FacturaEntity f
    JOIN FETCH f.ordenTrabajo ot
    JOIN FETCH ot.vehiculo v
    JOIN FETCH v.cliente c
    LEFT JOIN FETCH ot.items
    WHERE (:id IS NULL OR f.id = :id)
    AND (:numeroFactura IS NULL OR f.numeroFactura = :numeroFactura)
    AND (:clienteDni IS NULL OR c.dni = :clienteDni)
    AND (CAST(:fechaDesde AS date) IS NULL OR f.fechaEmision >= :fechaDesde)
    AND (CAST(:fechaHasta AS date) IS NULL OR f.fechaEmision <= :fechaHasta)
    """)
    List<FacturaEntity> findByFiltros(
            @Param("id") Long id,
            @Param("numeroFactura") String numeroFactura,
            @Param("clienteDni") String clienteDni,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta
    );
}