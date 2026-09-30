package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Factura;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FacturaRepository {
    Factura save(Factura factura);
    Optional<Factura> findById(Long id);
    List<Factura> findByFiltros(Long id, String numeroFactura, String clienteDni, String patenteVehiculo,
                                 Long ordenTrabajoId, LocalDate desde, LocalDate hasta);
    List<Factura> findActivasByOrdenTrabajoId(Long ordenTrabajoId);
    List<Factura> findActivasByOrdenTrabajoIds(List<Long> ordenTrabajoIds);
}
