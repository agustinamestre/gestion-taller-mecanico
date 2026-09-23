package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.PresupuestoEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.PresupuestoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaPresupuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        PresupuestoEntity entity = jpaPresupuestoRepository.findByIdConDetalle(presupuesto.getId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(presupuesto.getId())));

        mapper.updateEntity(presupuesto, entity);
        return mapper.toDomain(jpaPresupuestoRepository.save(entity));
    }

    @Override
    public Optional<Presupuesto> findById(Long id) {
        return jpaPresupuestoRepository.findByIdConDetalle(id).map(mapper::toDomain);
    }

    @Override
    public List<Presupuesto> findAll() {
        return jpaPresupuestoRepository.findAllConDetalle()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Presupuesto> buscar(String patente, String dni) {
        return jpaPresupuestoRepository.buscar(patente, dni).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Presupuesto> findByEstadoInAndFechaVencimientoBefore(List<EstadoPresupuesto> estados, LocalDate fecha) {
        return jpaPresupuestoRepository.findByEstadoInAndFechaVencimientoBefore(estados, fecha).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaPresupuestoRepository.deleteById(id);
    }

}