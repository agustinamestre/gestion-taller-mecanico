package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.*;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.OrdenTrabajoResponse;
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

@Tag(name = "Ordenes de Trabajo", description = "Operaciones sobre ordenes de trabajo del taller")
public interface SwaggerOrdenTrabajoController {

    @Operation(summary = "Registrar orden de trabajo",
            description = "Crea una nueva orden de trabajo. El vehículo se identifica por 'patente' o por 'presupuestoId' (al menos uno).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden registrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o regla de negocio violada (presupuesto no aprobado, presupuesto sin vehiculo, patente inconsistente con presupuesto, etc.)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Vehiculo o presupuesto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<OrdenTrabajoResponse> registrar(@Valid @RequestBody RegistrarOrdenTrabajoRequest request);

    @Operation(
            summary = "Listar ordenes de trabajo",
            description = "Retorna ordenes de trabajo. Se puede filtrar por patente y/o estado. Si no se envian filtros retorna todas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ordenes obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<List<OrdenTrabajoResponse>> obtenerOrdenes(
            @Parameter(description = "Patente del vehiculo (opcional)")
            @RequestParam(required = false) String patente,
            @Parameter(description = "Estado de la orden (opcional)")
            @RequestParam(required = false) EstadoOrdenTrabajo estado);

    @Operation(summary = "Cambiar estado de la orden", description = "Actualiza el estado de una orden respetando transiciones validas")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Transicion de estado invalida",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}/estado")
    ResponseEntity<Void> cambiarEstado(
            @Parameter(description = "ID de la orden de trabajo", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoOrdenTrabajoRequest request);

    @Operation(summary = "Modificar orden de trabajo", description = "Actualiza la descripcion del problema de una orden de trabajo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden modificada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    ResponseEntity<OrdenTrabajoResponse> modificar(
            @Parameter(description = "ID de la orden de trabajo", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ModificarOrdenTrabajoRequest request);

    @Operation(summary = "Agregar item a orden", description = "Agrega un nuevo item a una orden de trabajo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item agregado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Orden o producto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/items")
    ResponseEntity<OrdenTrabajoResponse> agregarItem(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable Long id,
            @Valid @RequestBody AgregarItemOrdenTrabajoRequest request);

    @Operation(summary = "Modificar item de orden", description = "Modifica cantidad, descripcion y precio unitario de un item existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item modificado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Orden, item o producto no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}/items/{itemId}")
    ResponseEntity<OrdenTrabajoResponse> modificarItem(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID del item", required = true)
            @PathVariable Long itemId,
            @Valid @RequestBody ModificarItemOrdenTrabajoRequest request);

    @Operation(summary = "Eliminar item de orden", description = "Elimina un item de una orden y recalcula el total automaticamente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Orden o item no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}/items/{itemId}")
    ResponseEntity<Void> eliminarItem(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID del item", required = true)
            @PathVariable Long itemId);
}
