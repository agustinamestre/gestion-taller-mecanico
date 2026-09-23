package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PresupuestoRepository {
    Presupuesto save(Presupuesto presupuesto);
    Optional<Presupuesto> findById(Long id);
    List<Presupuesto> findAll();
    List<Presupuesto> buscar(String patente, String dni);
    List<Presupuesto> findByEstadoInAndFechaVencimientoBefore(List<EstadoPresupuesto> estados, LocalDate fecha);
    void deleteById(Long id);
}
