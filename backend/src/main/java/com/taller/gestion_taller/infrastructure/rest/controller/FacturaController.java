package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.factura.AnularFacturaCommand;
import com.taller.gestion_taller.application.command.factura.ConsultarFacturasCommand;
import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.infrastructure.rest.controller.swagger.SwaggerFacturaController;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.request.AnularFacturaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.request.GenerarFacturaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.factura.response.FacturaResponse;
import com.taller.gestion_taller.infrastructure.rest.mapper.FacturaRestMapper;
import com.taller.gestion_taller.infrastructure.service.FacturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/facturas")
public class FacturaController implements SwaggerFacturaController {

    private final FacturaService facturaService;
    private final FacturaRestMapper facturaRestMapper;

    @Override
    public ResponseEntity<FacturaResponse> generarFactura(@Valid @RequestBody GenerarFacturaRequest request) {
        GenerarFacturaCommand command = facturaRestMapper.requestToCommand(request);
        Factura nuevaFactura = facturaService.generarFactura(command);
        FacturaResponse response = facturaRestMapper.domainToResponse(nuevaFactura);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<FacturaResponse>> consultarFacturas(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String numeroFactura,
            @RequestParam(required = false) String clienteDni) {

        ConsultarFacturasCommand query = ConsultarFacturasCommand.builder()
                .id(id)
                .numeroFactura(numeroFactura)
                .clienteDni(clienteDni)
                .build();

        List<FacturaResponse> response = facturaService.consultarFacturas(query)
                .stream()
                .map(facturaRestMapper::domainToResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FacturaResponse> anularFactura(@PathVariable Long id, @Valid @RequestBody AnularFacturaRequest request) {
        Factura anulada = facturaService.anularFactura(new AnularFacturaCommand(id, request.getMotivo()));
        return ResponseEntity.ok(facturaRestMapper.domainToResponse(anulada));
    }
}
