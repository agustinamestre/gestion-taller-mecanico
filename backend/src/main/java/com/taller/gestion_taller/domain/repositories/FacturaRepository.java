package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Factura;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository {
    Factura save(Factura factura);
    Optional<Factura> findById(Long id);
    List<Factura> findByFiltros(Long id, String numeroFactura, String clienteDni);
}
