package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
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
        if (presupuesto.getId() == null) {
            PresupuestoEntity entity = mapper.toEntity(presupuesto);
            return mapper.toDomain(jpaPresupuestoRepository.save(entity));
        }

        PresupuestoEntity entity = jpaPresupuestoRepository.findById(presupuesto.getId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(presupuesto.getId())));

        mapper.updateEntity(presupuesto, entity);
        return mapper.toDomain(jpaPresupuestoRepository.save(entity));
    }

    @Override
    public Optional<Presupuesto> findById(Long id) {
        return jpaPresupuestoRepository.findById(id).map(mapper::toDomain);
    }

}
