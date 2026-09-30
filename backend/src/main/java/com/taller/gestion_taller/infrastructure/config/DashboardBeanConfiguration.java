package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.dashboard.ObtenerResumenDashboard;
import com.taller.gestion_taller.application.usecases.dashboard.ObtenerResumenDashboardUseCase;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardBeanConfiguration {

    @Bean
    public ObtenerResumenDashboard obtenerResumenDashboardUseCase(OrdenTrabajoRepository ordenTrabajoRepository,
                                                                    PresupuestoRepository presupuestoRepository,
                                                                    FacturaRepository facturaRepository,
                                                                    AlertaRepository alertaRepository) {
        return new ObtenerResumenDashboardUseCase(
                ordenTrabajoRepository, presupuestoRepository, facturaRepository, alertaRepository);
    }
}
