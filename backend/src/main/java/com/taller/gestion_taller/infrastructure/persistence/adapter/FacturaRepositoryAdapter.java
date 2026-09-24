package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.FacturaEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.FacturaPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaFacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public List<Factura> findByFiltros(Long id, String numeroFactura, String clienteDni) {
        return jpaRepository.findByFiltros(id, numeroFactura, clienteDni)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
