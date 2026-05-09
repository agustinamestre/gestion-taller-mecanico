package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.presupuesto.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.ModificarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerPresupuestoController;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.ItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.PresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.response.PresupuestoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.presupuesto.request.ModificarItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.mapper.PresupuestoRestMapper;
import com.taller.gestion_taller.infrastructure.service.PresupuestoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/presupuestos")
@RequiredArgsConstructor
public class PresupuestoController implements SwaggerPresupuestoController {

    private final PresupuestoService presupuestoService;
    private final PresupuestoRestMapper presupuestoRestMapper;

    @Override
    public ResponseEntity<PresupuestoResponse> registrar(@Valid @RequestBody PresupuestoRequest request) {
        RegistrarPresupuestoCommand command = presupuestoRestMapper.requestToCommand(request);
        Presupuesto presupuesto = presupuestoService.registrarPresupuesto(command);
        PresupuestoResponse response = presupuestoRestMapper.domainToResponse(presupuesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<PresupuestoResponse> agregarItem(@PathVariable("id") Long presupuestoId,
                                                           @Valid @RequestBody ItemPresupuestoRequest request) {
        AgregarItemPresupuestoCommand command = presupuestoRestMapper.requestToAgregarItemCommand(presupuestoId, request);
        Presupuesto presupuesto = presupuestoService.agregarItem(command);
        PresupuestoResponse response = presupuestoRestMapper.domainToResponse(presupuesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<PresupuestoResponse> modificarItem(@PathVariable("id") Long presupuestoId,
                                                             @PathVariable("itemId") Long itemId,
                                                             @Valid @RequestBody ModificarItemPresupuestoRequest request) {
        ModificarItemPresupuestoCommand command = presupuestoRestMapper.requestToModificarItemCommand(
                presupuestoId, itemId, request);
        Presupuesto presupuesto = presupuestoService.modificarItem(command);
        PresupuestoResponse response = presupuestoRestMapper.domainToResponse(presupuesto);
        return ResponseEntity.ok(response);
    }
}