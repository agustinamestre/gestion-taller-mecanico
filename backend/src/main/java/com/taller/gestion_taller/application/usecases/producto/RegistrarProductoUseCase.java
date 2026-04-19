package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.application.command.RegistrarProductoCommand;
import com.taller.gestion_taller.application.mapper.ProductoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Producto;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarProductoUseCase implements RegistrarProducto {

    private final ProductoRepository productoRepository;
    private final ProductoApplicationMapper applicationMapper;

    @Override
    public Producto registrar(RegistrarProductoCommand command) {
        TipoProducto tipo = TipoProducto.from(command.tipo());

        if (productoRepository.existePorNombreYTipo(command.nombre(), tipo)) {
            throw new BusinessRunTimeException(BusinessErrors.productoDuplicado(command.nombre(), tipo));
        }

        Producto producto = applicationMapper.commandToDomain(command);

        return productoRepository.save(producto);
    }
}
