package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.dto.Reporte;
import com.taller.gestion_taller.application.usecases.reporte.ObtenerReporte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ObtenerReporte obtenerReporteUseCase;

    @Transactional(readOnly = true)
    public Reporte obtener(LocalDate desde, LocalDate hasta) {
        return obtenerReporteUseCase.obtener(desde, hasta);
    }
}
