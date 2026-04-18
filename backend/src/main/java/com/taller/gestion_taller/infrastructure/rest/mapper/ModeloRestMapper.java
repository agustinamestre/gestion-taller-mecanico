package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.ModificarModeloCommand;
import com.taller.gestion_taller.application.command.RegistrarModeloCommand;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.infrastructure.rest.dto.ModeloRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ModeloResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ModeloRestMapper {

    RegistrarModeloCommand requestToCommand(ModeloRequest request);

    ModificarModeloCommand requestToModificarCommand(ModeloRequest request);

    ModeloResponse domainToResponse(Modelo modelo);

    List<ModeloResponse> domainListToResponseList(List<Modelo> modelos);
}
