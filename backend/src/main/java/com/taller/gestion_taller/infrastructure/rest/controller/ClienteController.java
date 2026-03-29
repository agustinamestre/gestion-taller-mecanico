package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.RegistrarClienteCommand;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarCliente;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.infrastructure.rest.dto.ClienteRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ClienteResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.ClienteRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final RegistrarCliente registrarCliente;
    private final ClienteRestMapper clienteRestMapper;
    
    @PostMapping()
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRequest request) {
        RegistrarClienteCommand command = clienteRestMapper.requestToCommand(request);
        
        Cliente cliente = registrarCliente.registrarCliente(command);
        
        ClienteResponse response = clienteRestMapper.domainToResponse(cliente);
    
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
