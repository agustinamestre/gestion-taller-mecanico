package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.CambiarEstadoPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.ItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.ModificarItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.PresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.PresupuestoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.PresupuestoSummaryResponse;
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

@Tag(name = "Presupuestos", description = "Operaciones sobre presupuestos del taller")
public interface SwaggerPresupuestoController {

    @Operation(summary = "Registrar presupuesto", description = "Crea un nuevo presupuesto en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Presupuesto registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PresupuestoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<PresupuestoResponse> registrar(@Valid @RequestBody PresupuestoRequest request);

    @Operation(summary = "Obtener presupuesto", description = "Retorna el detalle completo de un presupuesto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Presupuesto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PresupuestoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Presupuesto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    ResponseEntity<PresupuestoResponse> obtener(
            @Parameter(description = "ID del presupuesto", required = true)
            @PathVariable Long id);

    @Operation(
            summary = "Buscar presupuestos por patente",
            description = "Retorna la lista de presupuestos asociados a un vehiculo por su patente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Presupuestos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PresupuestoSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<List<PresupuestoSummaryResponse>> obtenerPorPatente(
            @Parameter(description = "Patente del vehiculo", required = true)
            @RequestParam String patente);

    @Operation(summary = "Agregar ítem al presupuesto", description = "Agrega un nuevo ítem a un presupuesto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ítem agregado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PresupuestoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Presupuesto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/items")
    ResponseEntity<PresupuestoResponse> agregarItem(
            @Parameter(description = "ID del presupuesto", required = true)
            @PathVariable("id") Long presupuestoId,
            @Valid @RequestBody ItemPresupuestoRequest request);

    @Operation(summary = "Modificar ítem del presupuesto", description = "Modifica un ítem existente dentro de un presupuesto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ítem modificado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PresupuestoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Presupuesto o ítem no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}/items/{itemId}")
    ResponseEntity<PresupuestoResponse> modificarItem(
            @Parameter(description = "ID del presupuesto", required = true)
            @PathVariable("id") Long presupuestoId,
            @Parameter(description = "ID del ítem", required = true)
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody ModificarItemPresupuestoRequest request);

    @Operation(summary = "Eliminar item del presupuesto", description = "Elimina un item de un presupuesto en estado pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "El presupuesto no esta en estado pendiente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Presupuesto o item no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}/items/{itemId}")
    ResponseEntity<Void> eliminarItem(
            @Parameter(description = "ID del presupuesto", required = true)
            @PathVariable("id") Long presupuestoId,
            @Parameter(description = "ID del item", required = true)
            @PathVariable("itemId") Long itemId);

    @Operation(summary = "Cambiar estado del presupuesto", description = "Actualiza el estado de un presupuesto respetando transiciones validas")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Transicion de estado invalida",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Presupuesto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}/estado")
    ResponseEntity<Void> cambiarEstado(
            @Parameter(description = "ID del presupuesto", required = true)
            @PathVariable("id") Long presupuestoId,
            @Valid @RequestBody CambiarEstadoPresupuestoRequest request);
}