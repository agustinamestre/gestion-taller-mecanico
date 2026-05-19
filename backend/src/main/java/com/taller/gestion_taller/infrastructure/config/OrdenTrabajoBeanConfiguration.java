package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.OrdenTrabajoApplicationMapper;
import com.taller.gestion_taller.application.usecases.orden.*;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
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

    @Bean
    public CambiarEstadoOrdenTrabajo cambiarEstadoOrdenTrabajoUseCase(OrdenTrabajoRepository ordenTrabajoRepository) {
        return new CambiarEstadoOrdenTrabajoUseCase(ordenTrabajoRepository);
    }

    @Bean
    public ModificarOrdenTrabajo modificarOrdenTrabajoUseCase(OrdenTrabajoRepository ordenTrabajoRepository) {
        return new ModificarOrdenTrabajoUseCase(ordenTrabajoRepository);
    }

    @Bean
    public AgregarItemOrdenTrabajo agregarItemOrdenTrabajo(OrdenTrabajoRepository ordenTrabajoRepository,
                                                           ProductoRepository productoRepository) {
        return new AgregarItemOrdenTrabajoUseCase(ordenTrabajoRepository, productoRepository);
    }

    @Bean
    public ModificarItemOrdenTrabajo modificarItemOrdenTrabajo(OrdenTrabajoRepository ordenTrabajoRepository,
                                                               ProductoRepository productoRepository) {
        return new ModificarItemOrdenTrabajoUseCase(ordenTrabajoRepository, productoRepository);
    }

    @Bean
    public EliminarItemOrdenTrabajo eliminarItemOrdenTrabajo(OrdenTrabajoRepository ordenTrabajoRepository) {
        return new EliminarItemOrdenTrabajoUseCase(ordenTrabajoRepository);
    }

}
