package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.VehiculoEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.VehiculoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaVehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VehiculoRepositoryAdapter implements VehiculoRepository {

    private final JpaVehiculoRepository jpaVehiculoRepository;
    private final VehiculoPersistenceMapper mapper;

    @Override
    public Vehiculo save(Vehiculo vehiculo) {
        VehiculoEntity entity = mapper.toEntity(vehiculo);
        VehiculoEntity saved = jpaVehiculoRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Vehiculo> findById(Long id) {
        return jpaVehiculoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Vehiculo> findByPatente(String patente) {
        return jpaVehiculoRepository.findByPatente(patente).map(mapper::toDomain);
    }

    @Override
    public List<Vehiculo> findByClienteId(Long clienteId) {
        return jpaVehiculoRepository.findByClienteId(clienteId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
