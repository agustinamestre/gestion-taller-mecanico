package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.producto.ActualizarPrecioProductoCommand;
import com.taller.gestion_taller.application.command.producto.ActualizarStockProductoCommand;
import com.taller.gestion_taller.application.command.producto.ModificarProductoCommand;
import com.taller.gestion_taller.application.command.producto.RegistrarProductoCommand;
import com.taller.gestion_taller.application.usecases.producto.*;
import com.taller.gestion_taller.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final RegistrarProducto registrarProductoUseCase;
    private final BuscarProductoPorTipo buscarProductoPorTipoUseCase;
    private final ModificarProducto modificarProductoUseCase;
    private final ListarProductos listarProductosUseCase;
    private final DesactivarProducto desactivarProductoUseCase;
    private final ObtenerTiposProducto obtenerTiposProductoUseCase;
    private final ActualizarPrecioProducto actualizarPrecioProductoUseCase;
    private final ActualizarStockProducto actualizarStockProductoUseCase;

    @Transactional
    public Producto registrarProducto(RegistrarProductoCommand command) {
        return registrarProductoUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarProductoPorTipo(String tipo) {
        return buscarProductoPorTipoUseCase.buscarPorTipo(tipo);
    }

    @Transactional(readOnly = true)
    public List<String> obtenerTiposProducto() {
        return obtenerTiposProductoUseCase.obtener();
    }


    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return listarProductosUseCase.listar();
    }

    @Transactional
    public Producto modificarProducto(Long id, ModificarProductoCommand command) {
        return modificarProductoUseCase.modificar(id, command);
    }

    @Transactional
    public Producto actualizarPrecioProducto(Long id, ActualizarPrecioProductoCommand command) {
        return actualizarPrecioProductoUseCase.actualizar(id, command);
    }

    @Transactional
    public Producto actualizarStockProducto(Long id, ActualizarStockProductoCommand command) {
        return actualizarStockProductoUseCase.actualizar(id, command);
    }

    @Transactional
    public void desactivarProducto(Long id) {
        desactivarProductoUseCase.desactivar(id);
    }
}
