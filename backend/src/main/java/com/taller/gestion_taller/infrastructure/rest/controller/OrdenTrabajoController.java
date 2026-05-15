package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;
import com.taller.gestion_taller.application.command.orden.ModificarOrdenTrabajoCommand;
import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerOrdenTrabajoController;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.CambiarEstadoOrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.ModificarOrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.OrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.OrdenTrabajoResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.OrdenTrabajoRestMapper;
import com.taller.gestion_taller.infrastructure.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenTrabajoController implements SwaggerOrdenTrabajoController {

    private final OrdenTrabajoService ordenTrabajoService;
    private final OrdenTrabajoRestMapper ordenTrabajoRestMapper;

    @Override
    public ResponseEntity<OrdenTrabajoResponse> registrar(@Valid @RequestBody OrdenTrabajoRequest request) {
        RegistrarOrdenTrabajoCommand command = ordenTrabajoRestMapper.requestToCommand(request);
        OrdenTrabajo orden = ordenTrabajoService.registrarOrden(command);
        OrdenTrabajoResponse response = ordenTrabajoRestMapper.domainToResponse(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<OrdenTrabajoResponse>> obtenerOrdenes(
            @RequestParam(required = false) String patente,
            @RequestParam(required = false) EstadoOrdenTrabajo estado) {

        List<OrdenTrabajoResponse> response = ordenTrabajoService
                .obtenerOrdenes(patente, estado)
                .stream()
                .map(ordenTrabajoRestMapper::domainToResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoOrdenTrabajoRequest request) {

        CambiarEstadoOrdenTrabajoCommand command = ordenTrabajoRestMapper.toCambiarEstadoCommand(id, request);
        ordenTrabajoService.cambiarEstado(command);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<OrdenTrabajoResponse> modificar(
            @PathVariable Long id,
            @Valid @RequestBody ModificarOrdenTrabajoRequest request) {

        ModificarOrdenTrabajoCommand command = ordenTrabajoRestMapper.toModificarCommand(id, request);
        OrdenTrabajo orden = ordenTrabajoService.modificarOrden(command);
        OrdenTrabajoResponse response = ordenTrabajoRestMapper.domainToResponse(orden);
        return ResponseEntity.ok(response);
    }
}