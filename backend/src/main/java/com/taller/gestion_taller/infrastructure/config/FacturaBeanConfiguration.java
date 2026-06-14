package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.factura.GenerarFactura;
import com.taller.gestion_taller.application.usecases.factura.GenerarFacturaUseCase;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.service.FacturaValidator;
import com.taller.gestion_taller.infrastructure.service.FacturaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacturaBeanConfiguration {


    @Bean
    public FacturaValidator facturaValidator(OrdenTrabajoRepository ordenTrabajoRepository, FacturaRepository facturaRepository) {
        return new FacturaValidator(ordenTrabajoRepository, facturaRepository);
    }

    @Bean
    public GenerarFactura generarFacturaUseCase(FacturaRepository facturaRepository, FacturaValidator facturaValidator, OrdenTrabajoRepository ordenTrabajoRepository) {
        return new GenerarFacturaUseCase(facturaRepository, facturaValidator, ordenTrabajoRepository);
    }

    @Bean
    public FacturaService facturaService(GenerarFactura generarFacturaUseCase) {
        return new FacturaService(generarFacturaUseCase);
    }
}
