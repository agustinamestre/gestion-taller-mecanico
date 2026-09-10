package com.taller.gestion_taller.application.usecases.alerta;

import java.util.List;

public interface ListarAlertasConVehiculo {
    List<AlertaConVehiculo> listar(String patente, Boolean contactado);
}
