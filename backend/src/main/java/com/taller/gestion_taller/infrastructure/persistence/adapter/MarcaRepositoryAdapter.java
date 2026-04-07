package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.MarcaEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.MarcaPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaMarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MarcaRepositoryAdapter implements MarcaRepository {

    private final JpaMarcaRepository jpaMarcaRepository;
    private final MarcaPersistenceMapper persistenceMapper;

    @Override
    public Marca save(Marca marca) {
        try {
            MarcaEntity entity = persistenceMapper.toEntity(marca);
            MarcaEntity saved = jpaMarcaRepository.save(entity);
            return persistenceMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRunTimeException(BusinessErrors.marcaDuplicada(marca.getNombre()));
        }
    }

    @Override
    public List<Marca> findAll() {
        return jpaMarcaRepository.findAll()
                .stream()
                .map(persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Marca> findByNombre(String nombre) {
        return jpaMarcaRepository.findByNombre(nombre)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<Marca> findById(Long id) {
        return jpaMarcaRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        // Para la baja lógica, el caso de uso DesactivarMarca ya actualiza el estado y llama a guardar.
        // Este método 'eliminar' se mantendría para una eliminación física si fuera necesaria,
        // pero no es el caso para la "baja lógica" solicitada.
        // Por ahora, lo implementamos como una eliminación física si se llegara a usar directamente.
        jpaMarcaRepository.deleteById(id);
    }
}
