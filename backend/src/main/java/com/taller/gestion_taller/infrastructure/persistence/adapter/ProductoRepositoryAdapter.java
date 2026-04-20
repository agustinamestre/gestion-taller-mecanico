package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.ProductoEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ProductoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductoRepositoryAdapter implements ProductoRepository {

    private final JpaProductoRepository jpaProductoRepository;
    private final ProductoPersistenceMapper persistenceMapper;

    @Override
    public Producto save(Producto producto) {
        try {
            ProductoEntity entity = persistenceMapper.toEntity(producto);
            ProductoEntity saved = jpaProductoRepository.save(entity);
            return persistenceMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRunTimeException(BusinessErrors.productoDuplicado(producto.getNombre(), producto.getTipo()));
        }
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return jpaProductoRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public boolean existePorNombreYTipo(String nombre, TipoProducto tipo) {
        return jpaProductoRepository.existsByNombreAndTipo(nombre, tipo);
    }

    @Override
    public List<Producto> buscarPorTipo(TipoProducto tipo) {
        return jpaProductoRepository.findByTipo(tipo).stream()
                .map(persistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
