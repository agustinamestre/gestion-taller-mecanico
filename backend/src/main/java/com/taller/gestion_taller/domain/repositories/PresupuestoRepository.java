package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Presupuesto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PresupuestoRepository {
    Presupuesto save(Presupuesto presupuesto);
    Optional<Presupuesto> findById(Long id);
    List<Presupuesto> findByPatente(String patente);
    List<Presupuesto> findAll();
    List<Presupuesto> findByFechaEmisionBetween(LocalDate desde, LocalDate hasta);
    List<Presupuesto> findByPatenteAndFechaEmisionBetween(String patente, LocalDate desde, LocalDate hasta);
}
