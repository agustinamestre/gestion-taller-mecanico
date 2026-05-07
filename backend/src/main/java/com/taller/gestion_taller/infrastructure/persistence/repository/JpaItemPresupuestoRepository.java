package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.ItemPresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaItemPresupuestoRepository extends JpaRepository<ItemPresupuestoEntity, Long> { }
