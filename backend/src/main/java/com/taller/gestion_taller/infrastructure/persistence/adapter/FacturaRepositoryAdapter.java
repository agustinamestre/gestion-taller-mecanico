package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.FacturaEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.FacturaPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.FacturaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FacturaRepositoryAdapter implements FacturaRepository {

    private final FacturaJpaRepository jpaRepository;
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
    public Factura actualizarNumeroFactura(Long id, String numeroFactura) {
        FacturaEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.facturaNoEncontrada(id)));
        entity.setNumeroFactura(numeroFactura);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Factura> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Factura> findByOrdenTrabajoId(Long ordenTrabajoId) {
        return jpaRepository.findByOrdenTrabajoId(ordenTrabajoId).map(mapper::toDomain);
    }
}
