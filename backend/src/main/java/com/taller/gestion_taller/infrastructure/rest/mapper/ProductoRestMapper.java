package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.ModificarProductoCommand;
import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.infrastructure.rest.dto.ModificarProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.ProductoResponse;
import org.mapstruct.Mapper;

@Mapper
public interface ProductoRestMapper {
    RegistrarProductoCommand requestToCommand(ProductoRequest request);

    ModificarProductoCommand requestToModificarCommand(Long id, ModificarProductoRequest request);

    ProductoResponse domainToResponse(Producto producto);
}
