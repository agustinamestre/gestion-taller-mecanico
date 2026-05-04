package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.PresupuestoEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.PresupuestoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaPresupuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PresupuestoRepositoryAdapter implements PresupuestoRepository {

    private final JpaPresupuestoRepository jpaPresupuestoRepository;
    private final PresupuestoPersistenceMapper mapper;

    @Override
    public Presupuesto save(Presupuesto presupuesto) {
        PresupuestoEntity entity = mapper.toEntity(presupuesto);
        PresupuestoEntity saved = jpaPresupuestoRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Presupuesto> findById(Long id) {
        return jpaPresupuestoRepository.findById(id).map(mapper::toDomain);
    }

}
