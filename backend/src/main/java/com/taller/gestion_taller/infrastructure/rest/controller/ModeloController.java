package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.RegistrarModeloCommand;
import com.taller.gestion_taller.application.usecases.modelo.*;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.infrastructure.rest.dto.ModeloRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ModeloResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.ModeloRestMapper;
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
public class ModeloController {

    private final RegistrarModelo registrarModelo;
    private final ListarModelos listarModelos;

    private final ModeloRestMapper modeloRestMapper;

    @PostMapping()
    public ResponseEntity<ModeloResponse> registrar(@Valid @RequestBody ModeloRequest request) {
        RegistrarModeloCommand command = modeloRestMapper.requestToCommand(request);
        Modelo modelo = registrarModelo.registrar(command);
        ModeloResponse response = modeloRestMapper.domainToResponse(modelo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<ModeloResponse>> listar(@RequestParam(required = false) Long marcaId) {
        List<Modelo> modelos = listarModelos.listar(Optional.ofNullable(marcaId));
        List<ModeloResponse> response = modeloRestMapper.domainListToResponseList(modelos);
        return ResponseEntity.ok(response);
    }
}
