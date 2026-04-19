package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.ModificarMarcaCommand;
import com.taller.gestion_taller.application.command.RegistrarMarcaCommand;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.infrastructure.rest.dto.MarcaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.MarcaResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MarcaRestMapper {

    RegistrarMarcaCommand requestToCommand(MarcaRequest request);

    ModificarMarcaCommand requestToModificarCommand(MarcaRequest request);

    MarcaResponse domainToResponse(Marca marca);

    List<MarcaResponse> domainListToResponseList(List<Marca> marcas);
}
