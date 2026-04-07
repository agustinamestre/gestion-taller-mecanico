package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.ClienteApplicationMapper;
import com.taller.gestion_taller.application.mapper.MarcaApplicationMapper;
import com.taller.gestion_taller.application.usecases.cliente.*;
import com.taller.gestion_taller.application.usecases.marca.*;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.service.MarcaValidator;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ClienteRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.MarcaRestMapper;
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
    public MarcaApplicationMapper marcaApplicationMapper() {
        return Mappers.getMapper(MarcaApplicationMapper.class);
    }

    @Bean
    public MarcaRestMapper marcaRestMapper() {
        return Mappers.getMapper(MarcaRestMapper.class);
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

    @Bean
    public ListarCliente listarClienteUseCase(ClienteRepository clienteRepository) {
        return new ListarClienteUseCase(clienteRepository);
    }

    @Bean
    public ModificarCliente modificarClienteUseCase(ClienteRepository clienteRepository) {
        return new ModificarClienteUseCase(clienteRepository);
    }

    @Bean
    public DarDeBajaCliente darDeBajaClienteUseCase(ClienteRepository clienteRepository) {
        return new DarDeBajaClienteUseCase(clienteRepository);
    }

    @Bean
    public RegistrarMarca registrarMarcaUseCase(MarcaApplicationMapper marcaApplicationMapper, MarcaValidator marcaValidator, MarcaRepository marcaRepository) {
        return new RegistrarMarcaUseCase(marcaApplicationMapper, marcaValidator, marcaRepository);
    }

    @Bean
    public ModificarMarca modificarMarcaUseCase(MarcaRepository marcaRepository, MarcaValidator marcaValidator) {
        return new ModificarMarcaUseCase(marcaRepository, marcaValidator);
    }

    @Bean
    public ListarMarcas listarMarcasUseCase(MarcaRepository marcaRepository) {
        return new ListarMarcasUseCase(marcaRepository);
    }

    @Bean
    public DesactivarMarca desactivarMarcaUseCase(MarcaRepository marcaRepository) {
        return new DesactivarMarcaUseCase(marcaRepository);
    }

    @Bean
    public MarcaValidator marcaValidator (MarcaRepository marcaRepository) {
        return new MarcaValidator(marcaRepository);
    }

}
