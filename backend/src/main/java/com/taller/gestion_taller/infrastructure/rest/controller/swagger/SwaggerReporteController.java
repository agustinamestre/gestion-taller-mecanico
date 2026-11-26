package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.reporte.response.ReporteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "Reportes", description = "Indicadores y gráficos de la actividad del taller (solo ADMIN)")
public interface SwaggerReporteController {

    @Operation(summary = "Obtener reporte del período",
            description = "Retorna facturación, órdenes ingresadas y entregadas, servicios más realizados, "
                    + "facturación por forma de pago y tiempo promedio de reparación para el rango indicado. "
                    + "Sin fechas, devuelve los últimos 6 meses.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReporteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "El usuario no es administrador",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    ResponseEntity<ReporteResponse> obtener(
            @Parameter(description = "Fecha inicial (inclusive).", example = "2026-06-01")
            @RequestParam(required = false) LocalDate desde,
            @Parameter(description = "Fecha final (inclusive).", example = "2026-11-18")
            @RequestParam(required = false) LocalDate hasta
    );
}
