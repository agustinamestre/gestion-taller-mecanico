package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.marca.ModificarMarcaCommand;
import com.taller.gestion_taller.application.command.marca.RegistrarMarcaCommand;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.infrastructure.rest.dto.marca.request.MarcaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.marca.response.MarcaResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MarcaRestMapper {

    RegistrarMarcaCommand requestToCommand(MarcaRequest request);

    ModificarMarcaCommand requestToModificarCommand(MarcaRequest request);

    MarcaResponse domainToResponse(Marca marca);

    List<MarcaResponse> domainListToResponseList(List<Marca> marcas);
}
