package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.marca.ModificarMarcaCommand;
import com.taller.gestion_taller.application.command.marca.RegistrarMarcaCommand;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerMarcaController;
import com.taller.gestion_taller.infrastructure.rest.dto.marca.request.MarcaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.marca.response.MarcaResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.MarcaRestMapper;
import com.taller.gestion_taller.infrastructure.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/marcas")
@RequiredArgsConstructor
public class MarcaController implements SwaggerMarcaController {

    private final MarcaService marcaService;
    private final MarcaRestMapper marcaRestMapper;

    @Override
    public ResponseEntity<MarcaResponse> registrar(@Valid @RequestBody MarcaRequest request) {
        RegistrarMarcaCommand command = marcaRestMapper.requestToCommand(request);
        Marca marca = marcaService.registrarMarca(command);
        MarcaResponse response = marcaRestMapper.domainToResponse(marca);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<MarcaResponse>> listar() {
        List<Marca> marcas = marcaService.listarMarcas();
        List<MarcaResponse> response = marcaRestMapper.domainListToResponseList(marcas);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MarcaResponse> modificar(@PathVariable Long id,
                                                   @Valid @RequestBody MarcaRequest request) {
        ModificarMarcaCommand command = marcaRestMapper.requestToModificarCommand(request);
        Marca marca = marcaService.modificarMarca(id, command);
        MarcaResponse response = marcaRestMapper.domainToResponse(marca);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        marcaService.desactivarMarca(id);
        return ResponseEntity.noContent().build();
    }
}
