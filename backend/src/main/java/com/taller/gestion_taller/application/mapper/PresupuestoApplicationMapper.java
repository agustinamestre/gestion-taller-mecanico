package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.presupuesto.RegistrarPresupuestoCommand;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(imports = {EstadoPresupuesto.class, LocalDate.class})
public interface PresupuestoApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "estado", expression = "java(EstadoPresupuesto.PENDIENTE)")
    @Mapping(target = "fechaEmision", expression = "java(LocalDate.now())")
    @Mapping(target = "fechaVencimiento", expression = "java(LocalDate.now().plusDays(30))")
    Presupuesto commandToDomain(RegistrarPresupuestoCommand command);
}
