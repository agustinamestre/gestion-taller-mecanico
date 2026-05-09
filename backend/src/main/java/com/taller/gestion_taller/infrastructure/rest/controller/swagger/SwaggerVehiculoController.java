package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.ActualizarKilometrajeRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.ModificarVehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.VehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.response.VehiculoResponse;
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

@Tag(name = "Vehículos", description = "Operaciones sobre vehículos del taller")
public interface SwaggerVehiculoController {

    @Operation(summary = "Registrar vehículo", description = "Crea un nuevo vehículo en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehículo registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<VehiculoResponse> registrar(@Valid @RequestBody VehiculoRequest request);

    @Operation(summary = "Buscar vehículo por patente", description = "Retorna un vehículo según su patente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/patente/{patente}")
    ResponseEntity<VehiculoResponse> getVehiculoByPatente(
            @Parameter(description = "Patente del vehículo", required = true)
            @PathVariable String patente);

    @Operation(summary = "Modificar vehículo", description = "Actualiza los datos generales de un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo modificado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    ResponseEntity<VehiculoResponse> modificar(
            @Parameter(description = "ID del vehículo", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ModificarVehiculoRequest request);

    @Operation(summary = "Actualizar kilometraje", description = "Actualiza únicamente el kilometraje de un vehículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kilometraje actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}/kilometraje")
    ResponseEntity<VehiculoResponse> actualizarKilometraje(
            @Parameter(description = "ID del vehículo", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ActualizarKilometrajeRequest request);

    @Operation(summary = "Desactivar vehículo", description = "Desactiva lógicamente un vehículo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehículo desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del vehículo", required = true)
            @PathVariable Long id);
}