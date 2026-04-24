package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.VehiculoRestMapper;
import com.taller.gestion_taller.infrastructure.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final VehiculoRestMapper vehiculoRestMapper;

    @PostMapping
    public ResponseEntity<VehiculoResponse> registrar(@Valid @RequestBody VehiculoRequest request) {
        RegistrarVehiculoCommand command = vehiculoRestMapper.requestToCommand(request);
        Vehiculo vehiculo = vehiculoService.registrarVehiculo(command);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
