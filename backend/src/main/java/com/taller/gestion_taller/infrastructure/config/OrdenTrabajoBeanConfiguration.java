package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.OrdenTrabajoApplicationMapper;
import com.taller.gestion_taller.application.usecases.orden.ObtenerOrdenes;
import com.taller.gestion_taller.application.usecases.orden.ObtenerOrdenesUseCase;
import com.taller.gestion_taller.application.usecases.orden.RegistrarOrdenTrabajo;
import com.taller.gestion_taller.application.usecases.orden.RegistrarOrdenTrabajoUseCase;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdenTrabajoBeanConfiguration {

    @Bean
    public RegistrarOrdenTrabajo registrarOrdenTrabajoUseCase(OrdenTrabajoRepository ordenTrabajoRepository,
                                                              VehiculoRepository vehiculoRepository,
                                                              PresupuestoRepository presupuestoRepository,
                                                              OrdenTrabajoApplicationMapper ordenTrabajoApplicationMapper) {
        return new RegistrarOrdenTrabajoUseCase(
                ordenTrabajoRepository,
                vehiculoRepository,
                presupuestoRepository,
                ordenTrabajoApplicationMapper
        );
    }

    @Bean
    public ObtenerOrdenes obtenerOrdenesPorPatenteUseCase(OrdenTrabajoRepository ordenTrabajoRepository,
                                                          VehiculoRepository vehiculoRepository) {
        return new ObtenerOrdenesUseCase(
                ordenTrabajoRepository,
                vehiculoRepository
        );
    }

}
