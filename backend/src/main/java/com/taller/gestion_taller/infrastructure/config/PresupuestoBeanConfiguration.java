package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.usecases.cliente.RegistrarCliente;
import com.taller.gestion_taller.application.usecases.presupuesto.*;
import com.taller.gestion_taller.application.usecases.vehiculo.RegistrarVehiculo;
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
    public ObtenerPresupuesto obtenerPresupuestoUseCase(PresupuestoRepository presupuestoRepository) {
        return new ObtenerPresupuestoUseCase(
                presupuestoRepository
        );
    }

    @Bean
    public ObtenerPresupuestosPorPatente obtenerPresupuestosPorPatenteUseCase(PresupuestoRepository presupuestoRepository,
                                                                              VehiculoRepository vehiculoRepository) {
        return new ObtenerPresupuestosPorPatenteUseCase(
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

    @Bean
    public ModificarItemPresupuesto modificarItemPresupuesto(PresupuestoRepository presupuestoRepository,
                                                             ProductoRepository productoRepository) {
        return new ModificarItemPresupuestoUseCase(
                presupuestoRepository,
                productoRepository
        );
    }

    @Bean
    public EliminarItemPresupuesto eliminarItemPresupuesto(PresupuestoRepository presupuestoRepository) {
        return new EliminarItemPresupuestoUseCase(
                presupuestoRepository
        );
    }

    @Bean
    public CambiarEstadoPresupuesto cambiarEstadoPresupuesto(PresupuestoRepository presupuestoRepository) {
        return new CambiarEstadoPresupuestoUseCase(
                presupuestoRepository
        );
    }

    @Bean
    public AsociarVehiculoAPresupuesto asociarVehiculoAPresupuestoUseCase(
            PresupuestoRepository presupuestoRepository,
            VehiculoRepository vehiculoRepository,
            RegistrarVehiculo registrarVehiculoUseCase,
            RegistrarCliente registrarClienteUseCase) {
        return new AsociarVehiculoAPresupuestoUseCase(
                presupuestoRepository,
                vehiculoRepository,
                registrarVehiculoUseCase,
                registrarClienteUseCase
        );
    }
}
