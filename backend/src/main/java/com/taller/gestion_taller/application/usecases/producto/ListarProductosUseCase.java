package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarProductosUseCase implements ListarProductos {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }
}
