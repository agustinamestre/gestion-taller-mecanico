package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.ClienteApplicationMapper;
import com.taller.gestion_taller.application.usecases.cliente.DarDeBajaCliente;
import com.taller.gestion_taller.application.usecases.cliente.DarDeBajaClienteUseCase;
import com.taller.gestion_taller.application.usecases.cliente.ListarCliente;
import com.taller.gestion_taller.application.usecases.cliente.ListarClienteUseCase;
import com.taller.gestion_taller.application.usecases.cliente.ListarClientes;
import com.taller.gestion_taller.application.usecases.cliente.ListarClientesUseCase;
import com.taller.gestion_taller.application.usecases.cliente.ModificarCliente;
import com.taller.gestion_taller.application.usecases.cliente.ModificarClienteUseCase;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarCliente;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarClienteUseCase;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteBeanConfiguration {

    @Bean
    public RegistrarCliente registrarClienteUseCase(ClienteRepository clienteRepository,
                                                    ClienteApplicationMapper clienteApplicationMapper) {
        return new RegistrarClienteUseCase(clienteRepository, clienteApplicationMapper);
    }

    @Bean
    public ListarClientes listarClientesUseCase(ClienteRepository clienteRepository) {
        return new ListarClientesUseCase(clienteRepository);
    }

    @Bean
    public ListarCliente listarClienteUseCase(ClienteRepository clienteRepository,
                                              VehiculoRepository vehiculoRepository) {
        return new ListarClienteUseCase(clienteRepository, vehiculoRepository);
    }

    @Bean
    public ModificarCliente modificarClienteUseCase(ClienteRepository clienteRepository) {
        return new ModificarClienteUseCase(clienteRepository);
    }

    @Bean
    public DarDeBajaCliente darDeBajaClienteUseCase(ClienteRepository clienteRepository) {
        return new DarDeBajaClienteUseCase(clienteRepository);
    }
}
