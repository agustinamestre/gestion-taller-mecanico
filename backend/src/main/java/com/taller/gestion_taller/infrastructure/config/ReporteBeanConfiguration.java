package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.reporte.ObtenerReporte;
import com.taller.gestion_taller.application.usecases.reporte.ObtenerReporteUseCase;
import com.taller.gestion_taller.domain.repositories.ReporteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReporteBeanConfiguration {

    @Bean
    public ObtenerReporte obtenerReporteUseCase(ReporteRepository reporteRepository) {
        return new ObtenerReporteUseCase(reporteRepository);
    }
}
