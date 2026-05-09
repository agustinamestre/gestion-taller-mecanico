package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.cliente.request.ClienteRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.request.ModificarClienteRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.response.ClienteResponse;
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

@Tag(name = "Clientes", description = "Operaciones sobre clientes del taller")
public interface SwaggerClienteController {

    @Operation(summary = "Registrar cliente", description = "Crea un nuevo cliente en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRequest request);

    @Operation(summary = "Listar clientes", description = "Retorna todos los clientes activos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<List<ClienteResponse>> listar();

    @Operation(summary = "Buscar cliente", description = "Retorna un cliente por número de documento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{nroDocumento}")
    ResponseEntity<ClienteResponse> buscar(
            @Parameter(description = "Número de documento del cliente", required = true)
            @PathVariable String nroDocumento);

    @Operation(summary = "Modificar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente modificado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el request",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{nroDocumento}")
    ResponseEntity<ClienteResponse> modificar(
            @Parameter(description = "Número de documento del cliente", required = true)
            @PathVariable String nroDocumento,
            @Valid @RequestBody ModificarClienteRequest request);

    @Operation(summary = "Dar de baja cliente", description = "Desactiva lógicamente un cliente por número de documento")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error técnico",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{nroDocumento}")
    ResponseEntity<Void> desactivar(
            @Parameter(description = "Número de documento del cliente", required = true)
            @PathVariable String nroDocumento);
}