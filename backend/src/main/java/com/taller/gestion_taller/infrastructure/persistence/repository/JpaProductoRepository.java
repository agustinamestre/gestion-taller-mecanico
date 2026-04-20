package com.taller.gestion_taller.infrastructure.persistence.repository;

import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaProductoRepository extends JpaRepository<ProductoEntity, Long> {
    boolean existsByNombreAndTipo(String nombre, TipoProducto tipo);
    List<ProductoEntity> findByTipo(TipoProducto tipo);
}
