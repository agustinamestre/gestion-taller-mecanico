package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.ActualizarPrecioProductoCommand;
import com.taller.gestion_taller.application.command.ActualizarStockProductoCommand;
import com.taller.gestion_taller.application.command.ModificarProductoCommand;
import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.application.usecases.producto.ActualizarPrecioProducto;
import com.taller.gestion_taller.application.usecases.producto.ActualizarStockProducto;
import com.taller.gestion_taller.application.usecases.producto.BuscarProductoPorTipo;
import com.taller.gestion_taller.application.usecases.producto.DesactivarProducto;
import com.taller.gestion_taller.application.usecases.producto.ModificarProducto;
import com.taller.gestion_taller.application.usecases.producto.ObtenerTiposProducto;
import com.taller.gestion_taller.application.usecases.producto.RegistrarProducto;
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
