package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.modelo.request.ModeloRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.modelo.response.ModeloResponse;
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

@Tag(name = "Modelos", description = "Operaciones sobre modelos de vehículos")
public interface SwaggerModeloController {

    @Operation(summary = "Registrar modelo", description = "Crea un nuevo modelo en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Modelo registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModeloResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<ModeloResponse> registrar(@Valid @RequestBody ModeloRequest request);

    @Operation(
            summary = "Listar modelos",
            description = "Retorna todos los modelos activos. Si se envía 'marcaId', filtra por esa marca."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de modelos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModeloResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<List<ModeloResponse>> listar(
            @Parameter(description = "ID de la marca para filtrar modelos (opcional)")
            @RequestParam(required = false) Long marcaId);

    @Operation(summary = "Modificar modelo", description = "Actualiza los datos de un modelo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modelo modificado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModeloResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    ResponseEntity<ModeloResponse> modificar(
            @Parameter(description = "ID del modelo", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ModeloRequest request);

    @Operation(summary = "Desactivar modelo", description = "Desactiva lógicamente un modelo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Modelo desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del modelo", required = true)
            @PathVariable Long id);
}