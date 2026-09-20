package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.model.Vehiculo;

public interface ObtenerVehiculoPorId {
    Vehiculo obtener(Long id);
}
