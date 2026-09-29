package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.dto.DashboardResumen;
import com.taller.gestion_taller.application.usecases.dashboard.ObtenerResumenDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ObtenerResumenDashboard obtenerResumenDashboardUseCase;

    @Transactional(readOnly = true)
    public DashboardResumen obtenerResumen() {
        return obtenerResumenDashboardUseCase.obtener();
    }
}
