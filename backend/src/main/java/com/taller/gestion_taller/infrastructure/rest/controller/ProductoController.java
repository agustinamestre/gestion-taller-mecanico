package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.application.usecases.producto.RegistrarProducto;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.infrastructure.rest.dto.ProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ProductoResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.ProductoRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final RegistrarProducto registrarProducto;
    private final ProductoRestMapper productoRestMapper;

    @PostMapping
    public ResponseEntity<ProductoResponse> registrar(@Valid @RequestBody ProductoRequest request) {
        RegistrarProductoCommand command = productoRestMapper.requestToCommand(request);
        Producto producto = registrarProducto.registrar(command);
        ProductoResponse response = productoRestMapper.domainToResponse(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
