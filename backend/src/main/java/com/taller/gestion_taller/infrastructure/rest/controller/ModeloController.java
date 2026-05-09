package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.modelo.ModificarModeloCommand;
import com.taller.gestion_taller.application.command.modelo.RegistrarModeloCommand;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerModeloController;
import com.taller.gestion_taller.infrastructure.rest.dto.modelo.request.ModeloRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.modelo.response.ModeloResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.ModeloRestMapper;
import com.taller.gestion_taller.infrastructure.service.ModeloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/modelos")
@RequiredArgsConstructor
public class ModeloController implements SwaggerModeloController {

    private final ModeloService modeloService;
    private final ModeloRestMapper modeloRestMapper;

    @Override
    public ResponseEntity<ModeloResponse> registrar(@Valid @RequestBody ModeloRequest request) {
        RegistrarModeloCommand command = modeloRestMapper.requestToCommand(request);
        Modelo modelo = modeloService.registrarModelo(command);
        ModeloResponse response = modeloRestMapper.domainToResponse(modelo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<ModeloResponse>> listar(@RequestParam(required = false) Long marcaId) {
        List<Modelo> modelos = modeloService.listarModelos(Optional.ofNullable(marcaId));
        List<ModeloResponse> response = modeloRestMapper.domainListToResponseList(modelos);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ModeloResponse> modificar(@PathVariable Long id,
                                                    @Valid @RequestBody ModeloRequest request) {
        ModificarModeloCommand command = modeloRestMapper.requestToModificarCommand(request);
        Modelo modelo = modeloService.modificarModelo(id, command);
        ModeloResponse response = modeloRestMapper.domainToResponse(modelo);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        modeloService.desactivarModelo(id);
        return ResponseEntity.noContent().build();
    }
}