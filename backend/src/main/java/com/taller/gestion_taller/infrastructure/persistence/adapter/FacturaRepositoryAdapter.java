package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.FacturaEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.FacturaPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaFacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FacturaRepositoryAdapter implements FacturaRepository {

    private final JpaFacturaRepository jpaRepository;
    private final FacturaPersistenceMapper mapper;

    @Override
    public Factura save(Factura factura) {
        FacturaEntity entity = mapper.toEntity(factura);
        jpaRepository.save(entity);

        return factura.toBuilder()
                .id(entity.getId())
                .build();
    }

    @Override
    public Optional<Factura> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Factura> findByFiltros(Long id, String numeroFactura, String clienteDni, String patenteVehiculo,
                                        Long ordenTrabajoId, LocalDate desde, LocalDate hasta) {
        return jpaRepository.findByFiltros(id, numeroFactura, clienteDni, patenteVehiculo, ordenTrabajoId, desde, hasta)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Factura> findActivasByOrdenTrabajoId(Long ordenTrabajoId) {
        return jpaRepository.findActivasByOrdenTrabajoId(ordenTrabajoId, EstadoFactura.EMITIDA)
                .stream()
                .map(this::toFacturaResumida)
                .toList();
    }

    @Override
    public List<Factura> findActivasByOrdenTrabajoIds(List<Long> ordenTrabajoIds) {
        return jpaRepository.findActivasByOrdenTrabajoIds(ordenTrabajoIds, EstadoFactura.EMITIDA)
                .stream()
                .map(this::toFacturaResumida)
                .toList();
    }

    private Factura toFacturaResumida(FacturaEntity entity) {
        return Factura.builder()
                .id(entity.getId())
                .ordenTrabajo(OrdenTrabajo.builder().id(entity.getOrdenTrabajo().getId()).build())
                .estado(entity.getEstado())
                .tipoFactura(entity.getTipoFactura())
                .montoFacturado(entity.getMontoFacturado())
                .build();
    }
}
