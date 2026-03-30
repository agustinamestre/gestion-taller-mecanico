package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.RegistrarClienteCommand;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.infrastructure.rest.dto.ClienteRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ClienteResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ClienteRestMapper {

    RegistrarClienteCommand requestToCommand(ClienteRequest request);

    ClienteResponse domainToResponse(Cliente cliente);

    List<ClienteResponse> domainListToResponseList(List<Cliente> clientes);
}
