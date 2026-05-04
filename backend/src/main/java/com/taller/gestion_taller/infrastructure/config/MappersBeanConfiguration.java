package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.ClienteApplicationMapper;
import com.taller.gestion_taller.application.mapper.MarcaApplicationMapper;
import com.taller.gestion_taller.application.mapper.ModeloApplicationMapper;
import com.taller.gestion_taller.application.mapper.ProductoApplicationMapper;
import com.taller.gestion_taller.application.mapper.VehiculoApplicationMapper;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ModeloPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.mapper.PresupuestoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ProductoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.mapper.VehiculoPersistenceMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ClienteRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.MarcaRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ModeloRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.PresupuestoRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.ProductoRestMapper;
import com.taller.gestion_taller.infrastructure.rest.mapper.VehiculoRestMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de todos los mappers de MapStruct.
 */
@Configuration
public class MappersBeanConfiguration {

    // Application mappers
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
    public VehiculoApplicationMapper vehiculoApplicationMapper() {
        return Mappers.getMapper(VehiculoApplicationMapper.class);
    }

    // Rest mappers
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
    public VehiculoRestMapper vehiculoRestMapper() {
        return Mappers.getMapper(VehiculoRestMapper.class);
    }
    
    @Bean
    public PresupuestoRestMapper presupuestoRestMapper() {
        return Mappers.getMapper(PresupuestoRestMapper.class);
    }
    
    // Persistence mappers
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
    public VehiculoPersistenceMapper vehiculoPersistenceMapper() {
        return Mappers.getMapper(VehiculoPersistenceMapper.class);
    }

    @Bean
    public PresupuestoPersistenceMapper presupuestoPersistenceMapper() {
        return Mappers.getMapper(PresupuestoPersistenceMapper.class);
    }
}
