package com.taller.gestion_taller.application.usecases.reporte;

import com.taller.gestion_taller.application.dto.Reporte;

import java.time.LocalDate;

public interface ObtenerReporte {
    Reporte obtener(LocalDate desde, LocalDate hasta);
}
