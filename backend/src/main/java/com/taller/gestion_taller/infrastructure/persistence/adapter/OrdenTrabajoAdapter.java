package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.OrdenTrabajoEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.OrdenTrabajoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaOrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrdenTrabajoAdapter implements OrdenTrabajoRepository {

    private final JpaOrdenTrabajoRepository ordenTrabajoJpaRepository;
    private final OrdenTrabajoPersistenceMapper ordenTrabajoPersistenceMapper;

    @Override
    public OrdenTrabajo save(OrdenTrabajo ordenTrabajo) {
        OrdenTrabajoEntity entity = ordenTrabajoPersistenceMapper.toEntity(ordenTrabajo);

        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setOrden(entity));
        }

        return ordenTrabajoPersistenceMapper.toDomain(ordenTrabajoJpaRepository.save(entity));
    }

    @Override
    public Optional<OrdenTrabajo> findById(Long id) {
        return ordenTrabajoJpaRepository.findById(id)
                .map(ordenTrabajoPersistenceMapper::toDomain);
    }

    @Override
    public List<OrdenTrabajo> findByFiltros(String patente, EstadoOrdenTrabajo estado) {
        return ordenTrabajoJpaRepository.findByFiltros(patente, estado)
                .stream()
                .map(ordenTrabajoPersistenceMapper::toDomain)
                .toList();
    }
}
