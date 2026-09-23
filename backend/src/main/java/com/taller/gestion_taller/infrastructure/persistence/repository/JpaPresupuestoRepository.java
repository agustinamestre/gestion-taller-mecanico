package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.infrastructure.persistence.entity.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPresupuestoRepository extends JpaRepository<PresupuestoEntity, Long> {

    List<PresupuestoEntity> findByEstadoInAndFechaVencimientoBefore(List<EstadoPresupuesto> estados, LocalDate fecha);

    @Query("""
        SELECT DISTINCT p FROM PresupuestoEntity p
        LEFT JOIN FETCH p.vehiculo v
        LEFT JOIN FETCH v.cliente
        LEFT JOIN FETCH v.modelo mo
        LEFT JOIN FETCH mo.marca
        LEFT JOIN FETCH p.items i
        LEFT JOIN FETCH i.producto
        WHERE (:patente IS NULL OR v.patente = :patente)
        AND (:dni IS NULL OR p.dni = :dni)
    """)
    List<PresupuestoEntity> buscar(@Param("patente") String patente, @Param("dni") String dni);

    @Query("""
        SELECT DISTINCT p FROM PresupuestoEntity p
        LEFT JOIN FETCH p.vehiculo v
        LEFT JOIN FETCH v.cliente
        LEFT JOIN FETCH v.modelo mo
        LEFT JOIN FETCH mo.marca
        LEFT JOIN FETCH p.items i
        LEFT JOIN FETCH i.producto
    """)
    List<PresupuestoEntity> findAllConDetalle();

    @Query("""
        SELECT DISTINCT p FROM PresupuestoEntity p
        LEFT JOIN FETCH p.vehiculo v
        LEFT JOIN FETCH v.cliente
        LEFT JOIN FETCH v.modelo mo
        LEFT JOIN FETCH mo.marca
        LEFT JOIN FETCH p.items i
        LEFT JOIN FETCH i.producto
        WHERE p.id = :id
    """)
    Optional<PresupuestoEntity> findByIdConDetalle(@Param("id") Long id);
}