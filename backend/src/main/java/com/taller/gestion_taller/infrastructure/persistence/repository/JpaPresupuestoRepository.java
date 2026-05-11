package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPresupuestoRepository extends JpaRepository<PresupuestoEntity, Long> {

    @Query("SELECT p FROM PresupuestoEntity p JOIN p.vehiculo v WHERE v.patente = :patente")
    List<PresupuestoEntity> findByVehiculoPatente(@Param("patente") String patente);
}
