package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.ClienteApplicationMapper;
import com.taller.gestion_taller.application.mapper.MarcaApplicationMapper;
import com.taller.gestion_taller.application.mapper.ModeloApplicationMapper;
import com.taller.gestion_taller.application.mapper.ProductoApplicationMapper;
import com.taller.gestion_taller.application.usecases.cliente.*;
import com.taller.gestion_taller.application.usecases.marca.*;
import com.taller.gestion_taller.application.usecases.modelo.*;
import com.taller.gestion_taller.application.usecases.producto.*;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import com.taller.gestion_taller.domain.service.MarcaValidator;
import com.taller.gestion_taller.domain.service.ModeloValidator;
import com.taller.gestion_taller.domain.service.ProductoValidator;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ModeloPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ProductoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ClienteRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.MarcaRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ModeloRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ProductoRestMapper;
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
    public ModeloApplicationMapper modeloApplicationMapper() {
        return Mappers.getMapper(ModeloApplicationMapper.class);
    }

    @Bean
    public ProductoApplicationMapper productoApplicationMapper() {
        return Mappers.getMapper(ProductoApplicationMapper.class);
    }

    @Bean
    public MarcaRestMapper marcaRestMapper() {
        return Mappers.getMapper(MarcaRestMapper.class);
    }

    @Bean
    public ModeloRestMapper modeloRestMapper() {
        return Mappers.getMapper(ModeloRestMapper.class);
    }

    @Bean
    public ClienteRestMapper clienteRestMapper() {
        return Mappers.getMapper(ClienteRestMapper.class);
    }

    @Bean
    public ProductoRestMapper productoRestMapper() {
        return Mappers.getMapper(ProductoRestMapper.class);
    }

    @Bean
    public ClientePersistenceMapper clientePersistenceMapper() {
        return Mappers.getMapper(ClientePersistenceMapper.class);
    }

    @Bean
    public ModeloPersistenceMapper modeloPersistenceMapper() {
        return Mappers.getMapper(ModeloPersistenceMapper.class);
    }

    @Bean
    public ProductoPersistenceMapper productoPersistenceMapper() {
        return Mappers.getMapper(ProductoPersistenceMapper.class);
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

    // MODELO BEANS
    @Bean
    public RegistrarModelo registrarModeloUseCase(ModeloRepository modeloRepository, MarcaRepository marcaRepository, ModeloApplicationMapper modeloApplicationMapper, ModeloValidator modeloValidator) {
        return new RegistrarModeloUseCase(modeloRepository, marcaRepository, modeloApplicationMapper, modeloValidator);
    }

    @Bean
    public ModeloValidator modeloValidator(ModeloRepository modeloRepository) {
        return new ModeloValidator(modeloRepository);
    }

    @Bean
    public ListarModelos listarModelosUseCase(ModeloRepository modeloRepository) {
        return new ListarModelosUseCase(modeloRepository);
    }

    @Bean
    public ModificarModelo modificarModeloUseCase(ModeloRepository modeloRepository, MarcaRepository marcaRepository, ModeloValidator modeloValidator) {
        return new ModificarModeloUseCase(modeloRepository, marcaRepository, modeloValidator);
    }

    @Bean
    public DesactivarModelo desactivarModeloUseCase(ModeloRepository modeloRepository) {
        return new DesactivarModeloUseCase(modeloRepository);
    }

    // PRODUCTO BEANS
    @Bean
    public RegistrarProducto registrarProductoUseCase(ProductoRepository productoRepository, ProductoApplicationMapper productoApplicationMapper) {
        return new RegistrarProductoUseCase(productoRepository, productoApplicationMapper);
    }

    @Bean
    public BuscarProductoPorTipo buscarProductoPorTipoUseCase(ProductoRepository productoRepository) {
        return new BuscarProductoPorTipoUseCase(productoRepository);
    }

    @Bean
    public ModificarProducto modificarProductoUseCase(ProductoRepository productoRepository, ProductoValidator productoValidator) {
        return new ModificarProductoUseCase(productoRepository, productoValidator);
    }

    @Bean
    public DesactivarProducto desactivarProductoUseCase(ProductoRepository productoRepository) {
        return new DesactivarProductoUseCase(productoRepository);
    }

    @Bean
    public ObtenerTiposProducto obtenerTiposProductoUseCase() {
        return new ObtenerTiposProductoUseCase();
    }

    @Bean
    public ProductoValidator productoValidator(ProductoRepository productoRepository) {
        return new ProductoValidator(productoRepository);
    }

}
