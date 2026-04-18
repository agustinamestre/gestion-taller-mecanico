package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.ModeloEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ModeloPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaModeloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ModeloRepositoryAdapter implements ModeloRepository {

    private final JpaModeloRepository jpaModeloRepository;
    private final ModeloPersistenceMapper persistenceMapper;

    @Override
    public Modelo save(Modelo modelo) {
        try {
            ModeloEntity entity = persistenceMapper.toEntity(modelo);
            ModeloEntity saved = jpaModeloRepository.save(entity);
            return persistenceMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRunTimeException(BusinessErrors.modeloDuplicado(modelo.getNombre(), modelo.getMarca().getNombre()));
        }
    }

    @Override
    public List<Modelo> findAll() {
        return jpaModeloRepository.findAll()
                .stream()
                .map(persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Modelo> findByMarcaId(Long marcaId) {
        return jpaModeloRepository.findByMarcaId(marcaId)
                .stream()
                .map(persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Modelo> findById(Long id) {
        return jpaModeloRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<Modelo> findByNombreAndMarcaId(String nombre, Long marcaId) {
        return jpaModeloRepository.findByNombreAndMarcaId(nombre, marcaId)
                .map(persistenceMapper::toDomain);
    }

}
