package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.orden.*;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
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
                                                              PresupuestoRepository presupuestoRepository) {
        return new RegistrarOrdenTrabajoUseCase(
                ordenTrabajoRepository,
                vehiculoRepository,
                presupuestoRepository
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
    public ObtenerOrdenTrabajoPorId obtenerOrdenTrabajoPorId(OrdenTrabajoRepository ordenTrabajoRepository) {
        return new ObtenerOrdenTrabajoPorIdUseCase(
                ordenTrabajoRepository
        );
    }

    @Bean
    public CambiarEstadoOrdenTrabajo cambiarEstadoOrdenTrabajoUseCase(OrdenTrabajoRepository ordenTrabajoRepository,
                                                                      PresupuestoRepository presupuestoRepository,
                                                                      VehiculoRepository vehiculoRepository,
                                                                      FacturaRepository facturaRepository) {
        return new CambiarEstadoOrdenTrabajoUseCase(ordenTrabajoRepository, presupuestoRepository, vehiculoRepository,
                facturaRepository);
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
                                                               ProductoRepository productoRepository,
                                                               FacturaRepository facturaRepository) {
        return new ModificarItemOrdenTrabajoUseCase(ordenTrabajoRepository, productoRepository, facturaRepository);
    }

    @Bean
    public EliminarItemOrdenTrabajo eliminarItemOrdenTrabajo(OrdenTrabajoRepository ordenTrabajoRepository,
                                                             FacturaRepository facturaRepository) {
        return new EliminarItemOrdenTrabajoUseCase(ordenTrabajoRepository, facturaRepository);
    }

    @Bean
    public VerificarFacturacionOrden verificarFacturacionOrdenUseCase(FacturaRepository facturaRepository) {
        return new VerificarFacturacionOrdenUseCase(facturaRepository);
    }

}
