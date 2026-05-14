package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerOrdenTrabajoController;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.request.OrdenTrabajoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.orden.response.OrdenTrabajoResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.OrdenTrabajoRestMapper;
import com.taller.gestion_taller.infrastructure.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}