package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.AgregarItemPresupuestoCommand;
import com.taller.gestion_taller.application.command.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.infrastructure.rest.dto.ItemPresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.PresupuestoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.PresupuestoResponse;
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
public class PresupuestoController {

    private final PresupuestoService presupuestoService;
    private final PresupuestoRestMapper presupuestoRestMapper;

    @PostMapping
    public ResponseEntity<PresupuestoResponse> registrar(@Valid @RequestBody PresupuestoRequest request) {
        RegistrarPresupuestoCommand command = presupuestoRestMapper.requestToCommand(request);
        Presupuesto presupuesto = presupuestoService.registrarPresupuesto(command);
        PresupuestoResponse response = presupuestoRestMapper.domainToResponse(presupuesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<PresupuestoResponse> agregarItem(@PathVariable("id") Long presupuestoId,
                                                           @Valid @RequestBody ItemPresupuestoRequest request) {
        AgregarItemPresupuestoCommand command = presupuestoRestMapper.requestToAgregarItemCommand(presupuestoId, request);
        Presupuesto presupuesto = presupuestoService.agregarItem(command);
        PresupuestoResponse response = presupuestoRestMapper.domainToResponse(presupuesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
