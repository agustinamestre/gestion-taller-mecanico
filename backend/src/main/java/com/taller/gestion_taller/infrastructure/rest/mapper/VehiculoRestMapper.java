package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.vehiculo.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.application.command.vehiculo.ModificarVehiculoCommand;
import com.taller.gestion_taller.application.command.vehiculo.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.ActualizarKilometrajeRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.cliente.response.ClienteSummaryResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.ModificarVehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.request.VehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.response.VehiculoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.vehiculo.response.VehiculoSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehiculoRestMapper {

    RegistrarVehiculoCommand requestToCommand(VehiculoRequest request);

    ModificarVehiculoCommand requestToModificarCommand(ModificarVehiculoRequest request);
    
    ActualizarKilometrajeCommand requestToActualizarKilometrajeCommand(ActualizarKilometrajeRequest request);

    @Mapping(source = "modelo.marca.nombre", target = "marca")
    @Mapping(source = "modelo.nombre", target = "modelo")
    @Mapping(source = "cliente", target = "cliente")
    VehiculoResponse domainToResponse(Vehiculo vehiculo);

    @Mapping(source = "modelo.marca.nombre", target = "marca")
    @Mapping(source = "modelo.nombre", target = "modelo")
    VehiculoSummaryResponse domainToSummaryResponse(Vehiculo vehiculo);

    ClienteSummaryResponse clienteToClienteSummaryResponse(Cliente cliente);
}
