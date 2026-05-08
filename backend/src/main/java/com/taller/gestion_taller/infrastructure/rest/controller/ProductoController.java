package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.producto.ActualizarPrecioProductoCommand;
import com.taller.gestion_taller.application.command.producto.ActualizarStockProductoCommand;
import com.taller.gestion_taller.application.command.producto.ModificarProductoCommand;
import com.taller.gestion_taller.application.command.producto.RegistrarProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ActualizarPrecioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ActualizarStockRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ModificarProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.response.ProductoResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.ProductoRestMapper;
import com.taller.gestion_taller.infrastructure.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoRestMapper productoRestMapper;

    @PostMapping
    public ResponseEntity<ProductoResponse> registrar(@Valid @RequestBody ProductoRequest request) {
        RegistrarProductoCommand command = productoRestMapper.requestToCommand(request);
        Producto producto = productoService.registrarProducto(command);
        ProductoResponse response = productoRestMapper.domainToResponse(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> obtenerTipos() {
        return ResponseEntity.ok(productoService.obtenerTiposProducto());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ProductoResponse>> buscarPorTipo(@PathVariable String tipo) {
        List<ProductoResponse> response = productoService.buscarProductoPorTipo(tipo)
                .stream()
                .map(productoRestMapper::domainToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> modificar(@PathVariable Long id, @Valid @RequestBody ModificarProductoRequest request) {
        ModificarProductoCommand command = productoRestMapper.requestToModificarCommand(id, request);
        Producto producto = productoService.modificarProducto(id, command);
        ProductoResponse response = productoRestMapper.domainToResponse(producto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/precio")
    public ResponseEntity<ProductoResponse> actualizarPrecio(@PathVariable Long id, @Valid @RequestBody ActualizarPrecioRequest request) {
        ActualizarPrecioProductoCommand command = productoRestMapper.requestToActualizarPrecioCommand(request);
        Producto producto = productoService.actualizarPrecioProducto(id, command);
        ProductoResponse response = productoRestMapper.domainToResponse(producto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(@PathVariable Long id, @Valid @RequestBody ActualizarStockRequest request) {
        ActualizarStockProductoCommand command = productoRestMapper.requestToActualizarStockCommand(request);
        Producto producto = productoService.actualizarStockProducto(id, command);
        ProductoResponse response = productoRestMapper.domainToResponse(producto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
