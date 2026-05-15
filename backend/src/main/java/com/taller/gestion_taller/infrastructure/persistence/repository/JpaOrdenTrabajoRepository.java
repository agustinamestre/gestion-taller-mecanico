package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.infrastructure.persistence.entity.OrdenTrabajoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaOrdenTrabajoRepository extends JpaRepository<OrdenTrabajoEntity, Long> {

    @EntityGraph(attributePaths = {"presupuesto", "presupuesto.items"})
    @Query("SELECT o FROM OrdenTrabajoEntity o JOIN o.vehiculo v " +
            "WHERE (:patente IS NULL OR v.patente = :patente) " +
            "AND (:estado IS NULL OR o.estado = :estado)")
    List<OrdenTrabajoEntity> findByFiltros(
            @Param("patente") String patente,
            @Param("estado") EstadoOrdenTrabajo estado);
}
