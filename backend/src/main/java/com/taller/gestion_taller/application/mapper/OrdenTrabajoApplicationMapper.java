package com.taller.gestion_taller.application.mapper;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(imports = {EstadoOrdenTrabajo.class, LocalDate.class})
public interface OrdenTrabajoApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "presupuesto", ignore = true)
    @Mapping(target = "fechaEgreso", ignore = true)
    @Mapping(target = "estado", expression = "java(EstadoOrdenTrabajo.INGRESADO)")
    @Mapping(target = "fechaIngreso", expression = "java(LocalDate.now())")
    OrdenTrabajo commandToDomain(RegistrarOrdenTrabajoCommand command);
}
