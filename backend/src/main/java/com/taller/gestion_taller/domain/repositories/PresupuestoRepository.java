package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.ItemPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;

import java.util.Optional;

public interface PresupuestoRepository {
    Presupuesto save(Presupuesto presupuesto);
    Optional<Presupuesto> findById(Long id);
}
