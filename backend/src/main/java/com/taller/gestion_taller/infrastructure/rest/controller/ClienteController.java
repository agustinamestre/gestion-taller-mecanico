package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.cliente.ModificarClienteCommand;
import com.taller.gestion_taller.application.command.cliente.RegistrarClienteCommand;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerClienteController;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.request.ClienteRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.response.ClienteResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.request.ModificarClienteRequest;
import com.taller.gestion_taller.infrastructure.rest.mapper.ClienteRestMapper;
import com.taller.gestion_taller.infrastructure.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/clientes")
@RequiredArgsConstructor
public class ClienteController implements SwaggerClienteController {

    private final ClienteService clienteService;
    private final ClienteRestMapper clienteRestMapper;

    @Override
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRequest request) {
        RegistrarClienteCommand command = clienteRestMapper.requestToCommand(request);
        Cliente cliente = clienteService.registrarCliente(command);
        ClienteResponse response = clienteRestMapper.domainToResponse(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<Cliente> clientes = clienteService.listarClientes();
        List<ClienteResponse> response = clienteRestMapper.domainListToResponseList(clientes);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ClienteResponse> buscar(@PathVariable String nroDocumento) {
        Cliente cliente = clienteService.listarCliente(nroDocumento);
        ClienteResponse response = clienteRestMapper.domainToResponse(cliente);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ClienteResponse> modificar(@PathVariable String nroDocumento,
                                                     @Valid @RequestBody ModificarClienteRequest request) {
        ModificarClienteCommand command = clienteRestMapper.requestToModificarCommand(nroDocumento, request);
        Cliente cliente = clienteService.modificarCliente(nroDocumento, command);
        ClienteResponse response = clienteRestMapper.domainToResponse(cliente);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> desactivar(@PathVariable String nroDocumento) {
        clienteService.darDeBajaCliente(nroDocumento);
        return ResponseEntity.noContent().build();
    }
}
