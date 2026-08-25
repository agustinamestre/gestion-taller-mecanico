package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.model.Vehiculo;

import java.util.List;

public interface ListarVehiculos {
    List<Vehiculo> listar(String patente);
}
