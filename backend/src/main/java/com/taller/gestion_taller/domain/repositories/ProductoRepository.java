package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    Producto save(Producto producto);
    Optional<Producto> findById(Long id);
    boolean existePorNombreYTipo(String nombre, TipoProducto tipo);
    List<Producto> buscarPorTipo(TipoProducto tipo);
}
