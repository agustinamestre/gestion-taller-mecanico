package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.model.OrdenTrabajo;

public interface ObtenerOrdenTrabajoPorId {
    OrdenTrabajo obtener(Long id);
}
