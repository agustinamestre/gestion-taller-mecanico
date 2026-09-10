package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Alerta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AlertaRepository {
    Alerta save(Alerta alerta);
    Optional<Alerta> findById(Long id);
    List<Alerta> findByFiltros(String patente, Boolean contactado);
    long countByContactadoFalse();
    boolean existsAlertaVigentePorVehiculo(Long vehiculoId, LocalDate fechaUltimoService);
}
