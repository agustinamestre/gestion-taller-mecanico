package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.infrastructure.persistence.entity.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaPresupuestoRepository extends JpaRepository<PresupuestoEntity, Long> {

    List<PresupuestoEntity> findByEstadoAndFechaVencimientoBefore(EstadoPresupuesto estado, LocalDate fecha);

    @Query("""
        SELECT p FROM PresupuestoEntity p
        LEFT JOIN p.vehiculo v
        WHERE (:patente IS NULL OR v.patente = :patente)
        AND (:dni IS NULL OR p.dni = :dni)
    """)
    List<PresupuestoEntity> buscar(@Param("patente") String patente, @Param("dni") String dni);
}
