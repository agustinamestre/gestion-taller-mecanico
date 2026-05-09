package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.marca.request.MarcaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.marca.response.MarcaResponse;
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

@Tag(name = "Marcas", description = "Operaciones sobre marcas de vehículos")
public interface SwaggerMarcaController {

    @Operation(summary = "Registrar marca", description = "Crea una nueva marca en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Marca registrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MarcaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<MarcaResponse> registrar(@Valid @RequestBody MarcaRequest request);

    @Operation(summary = "Listar marcas", description = "Retorna todas las marcas activas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MarcaResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<List<MarcaResponse>> listar();

    @Operation(summary = "Modificar marca", description = "Actualiza los datos de una marca existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca modificada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MarcaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    ResponseEntity<MarcaResponse> modificar(
            @Parameter(description = "ID de la marca", required = true)
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequest request);

    @Operation(summary = "Desactivar marca", description = "Desactiva lógicamente una marca por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Marca desactivada correctamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> desactivar(
            @Parameter(description = "ID de la marca", required = true)
            @PathVariable Long id);
}