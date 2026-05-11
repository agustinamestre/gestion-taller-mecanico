package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.Presupuesto;

import java.util.List;

public interface ObtenerPresupuestosPorPatente {
    List<Presupuesto> obtener(String patente);
}
