package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.Presupuesto;

public interface ObtenerPresupuesto {
    Presupuesto obtener(Long id);
}
