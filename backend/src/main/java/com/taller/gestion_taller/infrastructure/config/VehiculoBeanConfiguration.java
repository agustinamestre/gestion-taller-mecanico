package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.VehiculoApplicationMapper;
import com.taller.gestion_taller.application.usecases.vehiculo.ActualizarKilometraje;
import com.taller.gestion_taller.application.usecases.vehiculo.ActualizarKilometrajeUseCase;
import com.taller.gestion_taller.application.usecases.vehiculo.DesactivarVehiculo;
import com.taller.gestion_taller.application.usecases.vehiculo.DesactivarVehiculoUseCase;
import com.taller.gestion_taller.application.usecases.vehiculo.GetVehiculoByPatente;
import com.taller.gestion_taller.application.usecases.vehiculo.GetVehiculoByPatenteUseCase;
import com.taller.gestion_taller.application.usecases.vehiculo.ModificarVehiculo;
import com.taller.gestion_taller.application.usecases.vehiculo.ModificarVehiculoUseCase;
import com.taller.gestion_taller.application.usecases.vehiculo.RegistrarVehiculo;
import com.taller.gestion_taller.application.usecases.vehiculo.RegistrarVehiculoUseCase;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.VehiculoValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VehiculoBeanConfiguration {

    @Bean
    public VehiculoValidator vehiculoValidator(VehiculoRepository vehiculoRepository) {
        return new VehiculoValidator(vehiculoRepository);
    }

    @Bean
    public RegistrarVehiculo registrarVehiculoUseCase(VehiculoRepository vehiculoRepository,
                                                      ModeloRepository modeloRepository,
                                                      ClienteRepository clienteRepository,
                                                      VehiculoValidator vehiculoValidator,
                                                      VehiculoApplicationMapper vehiculoApplicationMapper) {
        return new RegistrarVehiculoUseCase(vehiculoRepository, modeloRepository, clienteRepository,
                vehiculoValidator, vehiculoApplicationMapper);
    }

    @Bean
    public GetVehiculoByPatente getVehiculoByPatenteUseCase(VehiculoRepository vehiculoRepository) {
        return new GetVehiculoByPatenteUseCase(vehiculoRepository);
    }

    @Bean
    public ModificarVehiculo modificarVehiculoUseCase(VehiculoRepository vehiculoRepository,
                                                      ModeloRepository modeloRepository,
                                                      ClienteRepository clienteRepository) {
        return new ModificarVehiculoUseCase(vehiculoRepository, modeloRepository, clienteRepository);
    }

    @Bean
    public ActualizarKilometraje actualizarKilometrajeUseCase(VehiculoRepository vehiculoRepository) {
        return new ActualizarKilometrajeUseCase(vehiculoRepository);
    }

    @Bean
    public DesactivarVehiculo desactivarVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        return new DesactivarVehiculoUseCase(vehiculoRepository);
    }
}
