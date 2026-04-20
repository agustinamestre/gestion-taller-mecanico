package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BuscarProductoPorTipoUseCase implements BuscarProductoPorTipo {
    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> buscarPorTipo(String tipo) {
        TipoProducto tipoProducto = TipoProducto.from(tipo);
        List<Producto> productos = productoRepository.buscarPorTipo(tipoProducto);
        if (productos.isEmpty()) {
            throw new NotFoundException(BusinessErrors.productosNoEncontradosPorTipo(tipo));
        }
        return productos;
    }
}
