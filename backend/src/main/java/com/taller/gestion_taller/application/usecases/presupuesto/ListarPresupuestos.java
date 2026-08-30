package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.Presupuesto;

import java.time.LocalDate;
import java.util.List;

public interface ListarPresupuestos {
    List<Presupuesto> listar(String patente, LocalDate fechaDesde, LocalDate fechaHasta);
}
