package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.application.usecases.alerta.AlertaConVehiculo;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.infrastructure.rest.dto.alerta.request.ContactarAlertaRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.alerta.response.AlertaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface AlertaRestMapper {

    @Mapping(target = "id", source = "alerta.id")
    @Mapping(target = "patenteVehiculo", source = "vehiculo.patente")
    @Mapping(target = "nombreCliente", source = "vehiculo.cliente", qualifiedByName = "nombreCompleto")
    AlertaResponse toResponse(Alerta alerta, Vehiculo vehiculo);

    default AlertaResponse toResponse(AlertaConVehiculo alertaConVehiculo) {
        return toResponse(alertaConVehiculo.alerta(), alertaConVehiculo.vehiculo());
    }

    List<AlertaResponse> toResponseList(List<AlertaConVehiculo> alertasConVehiculo);

    MarcarAlertaContactadaCommand toCommand(Long alertaId, ContactarAlertaRequest request);

    @Named("nombreCompleto")
    default String nombreCompleto(Cliente cliente) {
        return cliente.getNombre() + " " + cliente.getApellido();
    }
}
