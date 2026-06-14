package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.factura.request.GenerarFacturaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.response.FacturaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Facturas", description = "Gestión de facturas.")
public interface SwaggerFacturaController {

    @Operation(
            summary = "Generar una nueva factura a partir de una orden de trabajo.",
            description = "Crea una factura asociada a una orden de trabajo que esté en estado 'FINALIZADO' o 'ENTREGADO'. La orden no debe tener una factura previa.",
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
}
