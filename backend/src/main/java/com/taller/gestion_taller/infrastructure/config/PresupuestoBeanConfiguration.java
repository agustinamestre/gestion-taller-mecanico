package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.presupuesto.AgregarItemPresupuesto;
import com.taller.gestion_taller.application.usecases.presupuesto.AgregarItemPresupuestoUseCase;
import com.taller.gestion_taller.application.usecases.presupuesto.RegistrarPresupuesto;
import com.taller.gestion_taller.application.usecases.presupuesto.RegistrarPresupuestoUseCase;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PresupuestoBeanConfiguration {

    @Bean
    public RegistrarPresupuesto registrarPresupuestoUseCase(PresupuestoRepository presupuestoRepository,
                                                            VehiculoRepository vehiculoRepository) {
        return new RegistrarPresupuestoUseCase(
                presupuestoRepository,
                vehiculoRepository
        );
    }

    @Bean
    public AgregarItemPresupuesto agregarItemPresupuestoUseCase(PresupuestoRepository presupuestoRepository,
                                                                ProductoRepository productoRepository) {
        return new AgregarItemPresupuestoUseCase(
                presupuestoRepository,
                productoRepository
        );
    }
}
