package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.model.TipoProducto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObtenerTiposProductoUseCase implements ObtenerTiposProducto {

    @Override
    public List<String> obtener() {
        return Arrays.stream(TipoProducto.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
