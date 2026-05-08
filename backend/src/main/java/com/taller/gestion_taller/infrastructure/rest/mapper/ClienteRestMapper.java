package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.cliente.ModificarClienteCommand;
import com.taller.gestion_taller.application.command.cliente.RegistrarClienteCommand;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.request.ClienteRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.response.ClienteResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.request.ModificarClienteRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = VehiculoRestMapper.class)
public interface ClienteRestMapper {

    RegistrarClienteCommand requestToCommand(ClienteRequest request);

    ModificarClienteCommand requestToModificarCommand(String nroDocumento, ModificarClienteRequest request);

    ClienteResponse domainToResponse(Cliente cliente);

    List<ClienteResponse> domainListToResponseList(List<Cliente> clientes);
}
