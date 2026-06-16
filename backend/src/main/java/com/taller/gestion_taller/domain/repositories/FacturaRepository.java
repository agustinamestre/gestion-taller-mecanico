package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Factura;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FacturaRepository {
    Factura save(Factura factura);
    Optional<Factura> findById(Long id);
    Optional<Factura> findByOrdenTrabajoId(Long ordenTrabajoId);
    Factura actualizarNumeroFactura(Long id, String numeroFactura);
    List<Factura> findByFiltros(Long id, String numeroFactura, String clienteDni,
                                LocalDate fechaDesde, LocalDate fechaHasta);
}
