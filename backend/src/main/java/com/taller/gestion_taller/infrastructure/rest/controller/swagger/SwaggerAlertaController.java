package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.alerta.request.ContactarAlertaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.alerta.response.AlertaResponse;
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

@Tag(name = "Alertas", description = "Alertas de service vencido de vehiculos")
public interface SwaggerAlertaController {

    @Operation(
            summary = "Listar alertas",
            description = "Retorna las alertas, filtrando opcionalmente por patente del vehiculo y/o si ya fueron contactadas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alertas encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertaResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<List<AlertaResponse>> listar(
            @Parameter(description = "Patente del vehiculo (opcional)")
            @RequestParam(required = false) String patente,
            @Parameter(description = "Filtrar por alertas contactadas o no (opcional)")
            @RequestParam(required = false) Boolean contactado);

    @Operation(summary = "Contar alertas pendientes", description = "Retorna la cantidad de alertas no contactadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cantidad de alertas pendientes",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/pendientes/count")
    ResponseEntity<Long> contarPendientes();

    @Operation(summary = "Marcar alerta como contactada", description = "Registra que el encargado ya se comunico con el cliente por su cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerta marcada como contactada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertaResponse.class))),
            @ApiResponse(responseCode = "400", description = "La alerta ya estaba contactada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}/contactar")
    ResponseEntity<AlertaResponse> contactar(
            @Parameter(description = "ID de la alerta", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ContactarAlertaRequest request);
}
