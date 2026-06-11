package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.orden.*;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerOrdenTrabajoController;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.*;
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
    public ResponseEntity<OrdenTrabajoResponse> registrar(@Valid @RequestBody RegistrarOrdenTrabajoRequest request) {
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
    public ResponseEntity<OrdenTrabajoResponse> obtenerOrdenPorId(@PathVariable Long id) {
        OrdenTrabajo orden = ordenTrabajoService.obtenerOrdenPorId(id);
        OrdenTrabajoResponse response = ordenTrabajoRestMapper.domainToResponse(orden);
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

    @Override
    public ResponseEntity<OrdenTrabajoResponse> agregarItem(
            @PathVariable Long id,
            @Valid @RequestBody AgregarItemOrdenTrabajoRequest request) {
        AgregarItemOrdenTrabajoCommand command = ordenTrabajoRestMapper.toAgregarItemCommand(id, request);
        OrdenTrabajo orden = ordenTrabajoService.agregarItem(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenTrabajoRestMapper.domainToResponse(orden));
    }

    @Override
    public ResponseEntity<OrdenTrabajoResponse> modificarItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @Valid @RequestBody ModificarItemOrdenTrabajoRequest request) {
        ModificarItemOrdenTrabajoCommand command = ordenTrabajoRestMapper.toModificarItemCommand(id, itemId, request);
        OrdenTrabajo orden = ordenTrabajoService.modificarItem(command);
        return ResponseEntity.ok(ordenTrabajoRestMapper.domainToResponse(orden));
    }

    @Override
    public ResponseEntity<Void> eliminarItem(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        EliminarItemOrdenTrabajoCommand command = ordenTrabajoRestMapper.toEliminarItemCommand(id, itemId);
        ordenTrabajoService.eliminarItem(command);
        return ResponseEntity.noContent().build();
    }
}