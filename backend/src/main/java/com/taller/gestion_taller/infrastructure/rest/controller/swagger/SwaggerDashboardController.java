package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.dashboard.response.DashboardResumenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Dashboard", description = "Resumen del estado actual del taller")
public interface SwaggerDashboardController {

    @Operation(summary = "Obtener resumen del dashboard",
            description = "Retorna los indicadores mostrados en las tarjetas de la pagina principal (post-login)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumen obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DashboardResumenResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error tecnico",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/resumen")
    ResponseEntity<DashboardResumenResponse> obtenerResumen();
}
