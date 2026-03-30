package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.ClienteApplicationMapper;
import com.taller.gestion_taller.application.usecases.cliente.ListarClientes;
import com.taller.gestion_taller.application.usecases.cliente.ListarClientesUseCase;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarCliente;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarClienteUseCase;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ClienteRestMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ClienteApplicationMapper clienteApplicationMapper() {
        return Mappers.getMapper(ClienteApplicationMapper.class);
    }

    @Bean
    public ClienteRestMapper clienteRestMapper() {
        return Mappers.getMapper(ClienteRestMapper.class);
    }

    @Bean
    public ClientePersistenceMapper clientePersistenceMapper() {
        return Mappers.getMapper(ClientePersistenceMapper.class);
    }

    @Bean
    public RegistrarCliente registrarClienteUseCase(ClienteRepository clienteRepository, ClienteApplicationMapper clienteApplicationMapper) {
        return new RegistrarClienteUseCase(clienteRepository, clienteApplicationMapper);
    }

    @Bean
    public ListarClientes listarClientesUseCase(ClienteRepository clienteRepository) {
        return new ListarClientesUseCase(clienteRepository);
    }
}
