package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.model.Producto;

import java.util.List;

public interface BuscarProductoPorTipo {
    List<Producto> buscarPorTipo(String tipo);
}
