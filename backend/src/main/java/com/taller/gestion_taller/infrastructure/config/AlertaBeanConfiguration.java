package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.alerta.*;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertaBeanConfiguration {

    @Bean
    public GenerarAlertasService generarAlertasServiceUseCase(VehiculoRepository vehiculoRepository,
                                                               AlertaRepository alertaRepository) {
        return new GenerarAlertasServiceUseCase(
                vehiculoRepository,
                alertaRepository
        );
    }

    @Bean
    public MarcarAlertaContactada marcarAlertaContactadaUseCase(AlertaRepository alertaRepository) {
        return new MarcarAlertaContactadaUseCase(
                alertaRepository
        );
    }

    @Bean
    public ListarAlertasConVehiculo listarAlertasConVehiculoUseCase(AlertaRepository alertaRepository,
                                                                     VehiculoRepository vehiculoRepository) {
        return new ListarAlertasConVehiculoUseCase(
                alertaRepository,
                vehiculoRepository
        );
    }

    @Bean
    public ContarAlertasPendientes contarAlertasPendientesUseCase(AlertaRepository alertaRepository) {
        return new ContarAlertasPendientesUseCase(
                alertaRepository
        );
    }
}
