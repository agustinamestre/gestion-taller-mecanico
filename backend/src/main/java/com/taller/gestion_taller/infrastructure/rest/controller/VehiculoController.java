package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.application.command.ModificarVehiculoCommand;
import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.dto.ActualizarKilometrajeRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ModificarVehiculoRequest;
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

    @GetMapping("/patente/{patente}")
    public ResponseEntity<VehiculoResponse> getVehiculoByPatente(@PathVariable String patente) {
        Vehiculo vehiculo = vehiculoService.getVehiculoByPatente(patente);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponse> modificar(@PathVariable Long id, @Valid @RequestBody ModificarVehiculoRequest request) {
        ModificarVehiculoCommand command = vehiculoRestMapper.requestToModificarCommand(request);
        Vehiculo vehiculo = vehiculoService.modificarVehiculo(id, command);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/kilometraje")
    public ResponseEntity<VehiculoResponse> actualizarKilometraje(@PathVariable Long id, @Valid @RequestBody ActualizarKilometrajeRequest request) {
        ActualizarKilometrajeCommand command = vehiculoRestMapper.requestToActualizarKilometrajeCommand(request);
        Vehiculo vehiculo = vehiculoService.actualizarKilometraje(id, command);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        vehiculoService.desactivarVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}
