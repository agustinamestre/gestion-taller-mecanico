package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.AlertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaAlertaRepository extends JpaRepository<AlertaEntity, Long> {

    @Query("SELECT a FROM AlertaEntity a, VehiculoEntity v " +
            "WHERE a.vehiculoId = v.id " +
            "AND (:patente IS NULL OR v.patente = :patente) " +
            "AND (:contactado IS NULL OR a.contactado = :contactado) " +
            "ORDER BY a.fechaAlerta DESC")
    List<AlertaEntity> findByFiltros(@Param("patente") String patente, @Param("contactado") Boolean contactado);

    long countByContactadoFalse();

    boolean existsByVehiculoIdAndFechaAlertaGreaterThanEqual(Long vehiculoId, LocalDate fechaUltimoService);
}
