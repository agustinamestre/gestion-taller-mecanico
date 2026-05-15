package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;

import java.util.List;

public interface ObtenerOrdenes {
    List<OrdenTrabajo> obtener(String patente, EstadoOrdenTrabajo estado);
}