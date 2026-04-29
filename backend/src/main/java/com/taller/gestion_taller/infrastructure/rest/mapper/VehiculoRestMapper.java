package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.application.command.ModificarVehiculoCommand;
import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.dto.ActualizarKilometrajeRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ClienteSummaryResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.ModificarVehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.VehiculoSummaryResponse;
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
