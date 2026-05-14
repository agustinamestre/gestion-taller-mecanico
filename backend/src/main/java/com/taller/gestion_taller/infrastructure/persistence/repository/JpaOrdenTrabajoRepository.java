package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.infrastructure.persistence.entity.OrdenTrabajoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrdenTrabajoRepository extends JpaRepository<OrdenTrabajoEntity, Long> {}