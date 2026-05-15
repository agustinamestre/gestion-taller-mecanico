package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;

import java.util.List;
import java.util.Optional;

public interface OrdenTrabajoRepository {
    OrdenTrabajo save(OrdenTrabajo ordenTrabajo);
    Optional<OrdenTrabajo> findById(Long id);
    List<OrdenTrabajo> findByFiltros(String patente, EstadoOrdenTrabajo estado);
}
