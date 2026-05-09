package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.vehiculo.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.application.command.vehiculo.ModificarVehiculoCommand;
import com.taller.gestion_taller.application.command.vehiculo.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerVehiculoController;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.ActualizarKilometrajeRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.ModificarVehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.VehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.response.VehiculoResponse;
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
public class VehiculoController implements SwaggerVehiculoController {

    private final VehiculoService vehiculoService;
    private final VehiculoRestMapper vehiculoRestMapper;

    @Override
    public ResponseEntity<VehiculoResponse> registrar(@Valid @RequestBody VehiculoRequest request) {
        RegistrarVehiculoCommand command = vehiculoRestMapper.requestToCommand(request);
        Vehiculo vehiculo = vehiculoService.registrarVehiculo(command);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<VehiculoResponse> getVehiculoByPatente(@PathVariable String patente) {
        Vehiculo vehiculo = vehiculoService.getVehiculoByPatente(patente);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VehiculoResponse> modificar(@PathVariable Long id,
                                                      @Valid @RequestBody ModificarVehiculoRequest request) {
        ModificarVehiculoCommand command = vehiculoRestMapper.requestToModificarCommand(request);
        Vehiculo vehiculo = vehiculoService.modificarVehiculo(id, command);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VehiculoResponse> actualizarKilometraje(@PathVariable Long id,
                                                                  @Valid @RequestBody ActualizarKilometrajeRequest request) {
        ActualizarKilometrajeCommand command = vehiculoRestMapper.requestToActualizarKilometrajeCommand(request);
        Vehiculo vehiculo = vehiculoService.actualizarKilometraje(id, command);
        VehiculoResponse response = vehiculoRestMapper.domainToResponse(vehiculo);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        vehiculoService.desactivarVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}