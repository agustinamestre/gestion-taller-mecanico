package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehiculoApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modelo", source = "modelo")
    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "activo", constant = "true")
    Vehiculo commandToDomain(RegistrarVehiculoCommand command, Modelo modelo, Cliente cliente);
}
