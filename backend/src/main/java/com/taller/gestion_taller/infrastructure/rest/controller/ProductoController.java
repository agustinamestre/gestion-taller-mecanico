package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.ModificarProductoCommand;
import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.application.usecases.producto.*;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.infrastructure.rest.dto.ModificarProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ProductoResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.ProductoRestMapper;
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

    private final RegistrarProducto registrarProducto;
    private final BuscarProductoPorTipo buscarProductoPorTipo;
    private final ModificarProducto modificarProducto;
    private final DesactivarProducto desactivarProducto;
    private final ObtenerTiposProducto obtenerTiposProducto;
    private final ProductoRestMapper productoRestMapper;

    @PostMapping
    public ResponseEntity<ProductoResponse> registrar(@Valid @RequestBody ProductoRequest request) {
        RegistrarProductoCommand command = productoRestMapper.requestToCommand(request);
        Producto producto = registrarProducto.registrar(command);
        ProductoResponse response = productoRestMapper.domainToResponse(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> obtenerTipos() {
        return ResponseEntity.ok(obtenerTiposProducto.obtener());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ProductoResponse>> buscarPorTipo(@PathVariable String tipo) {
        List<ProductoResponse> response = buscarProductoPorTipo.buscarPorTipo(tipo)
                .stream()
                .map(productoRestMapper::domainToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> modificar(@PathVariable Long id, @Valid @RequestBody ModificarProductoRequest request) {
        ModificarProductoCommand command = productoRestMapper.requestToModificarCommand(id, request);
        Producto producto = modificarProducto.modificar(id, command);
        ProductoResponse response = productoRestMapper.domainToResponse(producto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        desactivarProducto.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
