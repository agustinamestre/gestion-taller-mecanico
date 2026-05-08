package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.producto.ActualizarPrecioProductoCommand;
import com.taller.gestion_taller.application.command.producto.ActualizarStockProductoCommand;
import com.taller.gestion_taller.application.command.producto.ModificarProductoCommand;
import com.taller.gestion_taller.application.command.producto.RegistrarProductoCommand;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ActualizarPrecioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ActualizarStockRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ModificarProductoRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.response.ProductoResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.producto.request.ProductoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductoRestMapper {
    RegistrarProductoCommand requestToCommand(ProductoRequest request);

    ModificarProductoCommand requestToModificarCommand(Long id, ModificarProductoRequest request);

    @Mapping(target = "nuevoPrecio", source = "request.nuevoPrecio")
    ActualizarPrecioProductoCommand requestToActualizarPrecioCommand(ActualizarPrecioRequest request);

    @Mapping(target = "nuevoStock", source = "request.nuevoStock")
    ActualizarStockProductoCommand requestToActualizarStockCommand(ActualizarStockRequest request);

    ProductoResponse domainToResponse(Producto producto);
}
