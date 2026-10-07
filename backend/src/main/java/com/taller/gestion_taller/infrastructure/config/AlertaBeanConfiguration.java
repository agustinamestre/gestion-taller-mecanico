package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.alerta.*;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.NotificadorCliente;
import com.taller.gestion_taller.domain.service.PoliticaServiceVehiculo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertaBeanConfiguration {

    @Bean
    public PoliticaServiceVehiculo politicaServiceVehiculo(@Value("${alertas.service.meses:12}") int meses,
                                                           @Value("${alertas.service.km:10000}") int km) {
        return new PoliticaServiceVehiculo(meses, km);
    }

    @Bean
    public GenerarAlertasService generarAlertasServiceUseCase(VehiculoRepository vehiculoRepository,
                                                               AlertaRepository alertaRepository,
                                                               PoliticaServiceVehiculo politicaServiceVehiculo,
                                                               OrdenTrabajoRepository ordenTrabajoRepository) {
        return new GenerarAlertasServiceUseCase(
                vehiculoRepository,
                alertaRepository,
                politicaServiceVehiculo,
                ordenTrabajoRepository
        );
    }

    @Bean
    public MarcarAlertaContactada marcarAlertaContactadaUseCase(AlertaRepository alertaRepository,
                                                                  VehiculoRepository vehiculoRepository,
                                                                  NotificadorCliente notificadorCliente) {
        return new MarcarAlertaContactadaUseCase(
                alertaRepository,
                vehiculoRepository,
                notificadorCliente
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
