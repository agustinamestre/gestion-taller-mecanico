package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ActualizarPrecioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ActualizarStockRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ModificarProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.response.ProductoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Operaciones sobre productos del taller")
public interface SwaggerProductoController {

    @Operation(summary = "Registrar producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<ProductoResponse> registrar(@Valid @RequestBody ProductoRequest request);

    @Operation(summary = "Listar productos", description = "Retorna todos los productos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<List<ProductoResponse>> listar();

    @Operation(summary = "Obtener tipos de producto", description = "Retorna la lista de tipos de producto disponibles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipos obtenidos correctamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/tipos")
    ResponseEntity<List<String>> obtenerTipos();

    @Operation(summary = "Buscar productos por tipo", description = "Retorna todos los productos del tipo indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Tipo inválido",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/tipo/{tipo}")
    ResponseEntity<List<ProductoResponse>> buscarPorTipo(
            @Parameter(description = "Tipo de producto", required = true)
            @PathVariable String tipo);

    @Operation(summary = "Modificar producto", description = "Actualiza los datos generales de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto modificado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    ResponseEntity<ProductoResponse> modificar(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ModificarProductoRequest request);

    @Operation(summary = "Actualizar precio", description = "Actualiza únicamente el precio de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Precio actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}/precio")
    ResponseEntity<ProductoResponse> actualizarPrecio(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPrecioRequest request);

    @Operation(summary = "Actualizar stock", description = "Actualiza únicamente el stock de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}/stock")
    ResponseEntity<ProductoResponse> actualizarStock(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ActualizarStockRequest request);

    @Operation(summary = "Desactivar producto", description = "Desactiva lógicamente un producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id);
}