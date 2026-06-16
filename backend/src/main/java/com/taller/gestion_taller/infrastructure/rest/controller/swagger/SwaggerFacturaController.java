package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.factura.request.GenerarFacturaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.response.FacturaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Facturas", description = "Gestión de facturas.")
public interface SwaggerFacturaController {

    @Operation(
            summary = "Generar una nueva factura a partir de una orden de trabajo.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Factura generada exitosamente.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FacturaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de negocio o datos inválidos. Por ejemplo, la orden no está en un estado facturable o ya tiene una factura.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La orden de trabajo especificada no fue encontrada.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    )
            }
    )
    @PostMapping
    ResponseEntity<FacturaResponse> generarFactura(@Valid @RequestBody GenerarFacturaRequest request);

    @Operation(
            summary = "Consultar facturas con filtros opcionales.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID único de la factura.",
                            example = "4"
                    ),
                    @Parameter(
                            name = "numeroFactura",
                            description = "Número único de la factura.",
                            example = "F00000004"
                    ),
                    @Parameter(
                            name = "clienteDni",
                            description = "DNI del cliente para filtrar todas sus facturas.",
                            example = "12345678"
                    ),
                    @Parameter(
                            name = "fechaDesde",
                            description = "Fecha de inicio del rango de emisión. Formato: yyyy-MM-dd.",
                            example = "2026-01-01"
                    ),
                    @Parameter(
                            name = "fechaHasta",
                            description = "Fecha de fin del rango de emisión. Formato: yyyy-MM-dd.",
                            example = "2026-06-30"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de facturas obtenido exitosamente. Puede ser una lista vacía si no hay resultados.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = FacturaResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parámetros inválidos. Por ejemplo, fechaDesde posterior a fechaHasta.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<FacturaResponse>> consultarFacturas(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String numeroFactura,
            @RequestParam(required = false) String clienteDni,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta
    );
}

