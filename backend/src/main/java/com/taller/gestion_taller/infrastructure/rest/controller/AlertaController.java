package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.application.usecases.alerta.AlertaConVehiculo;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerAlertaController;
import com.taller.gestion_taller.infrastructure.rest.dto.alerta.request.ContactarAlertaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.alerta.response.AlertaResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.AlertaRestMapper;
import com.taller.gestion_taller.infrastructure.service.AlertaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alertas")
@RequiredArgsConstructor
public class AlertaController implements SwaggerAlertaController {

    private final AlertaService alertaService;
    private final AlertaRestMapper alertaRestMapper;

    @Override
    public ResponseEntity<List<AlertaResponse>> listar(String patente, Boolean contactado) {
        List<AlertaConVehiculo> alertas = alertaService.listarConVehiculo(patente, contactado);
        return ResponseEntity.ok(alertaRestMapper.toResponseList(alertas));
    }

    @Override
    public ResponseEntity<Long> contarPendientes() {
        return ResponseEntity.ok(alertaService.contarPendientes());
    }

    @Override
    public ResponseEntity<AlertaResponse> contactar(Long id, @Valid ContactarAlertaRequest request) {
        MarcarAlertaContactadaCommand command = alertaRestMapper.toCommand(id, request);
        AlertaConVehiculo alertaConVehiculo = alertaService.marcarContactada(command);
        return ResponseEntity.ok(alertaRestMapper.toResponse(alertaConVehiculo));
    }
}
