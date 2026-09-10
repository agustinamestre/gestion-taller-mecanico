package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.Vehiculo;

public record AlertaConVehiculo(Alerta alerta, Vehiculo vehiculo) {
}
