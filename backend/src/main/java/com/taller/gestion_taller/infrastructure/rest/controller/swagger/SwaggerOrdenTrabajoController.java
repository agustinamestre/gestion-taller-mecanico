package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.OrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.OrdenTrabajoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Ordenes de Trabajo", description = "Operaciones sobre ordenes de trabajo del taller")
public interface SwaggerOrdenTrabajoController {

    @Operation(summary = "Registrar orden de trabajo", description = "Crea una nueva orden de trabajo para un vehiculo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden registrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<OrdenTrabajoResponse> registrar(@Valid @RequestBody OrdenTrabajoRequest request);
}
