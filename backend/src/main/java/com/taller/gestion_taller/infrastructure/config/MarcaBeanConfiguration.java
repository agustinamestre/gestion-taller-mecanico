package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.MarcaApplicationMapper;
import com.taller.gestion_taller.application.usecases.marca.DesactivarMarca;
import com.taller.gestion_taller.application.usecases.marca.DesactivarMarcaUseCase;
import com.taller.gestion_taller.application.usecases.marca.ListarMarcas;
import com.taller.gestion_taller.application.usecases.marca.ListarMarcasUseCase;
import com.taller.gestion_taller.application.usecases.marca.ModificarMarca;
import com.taller.gestion_taller.application.usecases.marca.ModificarMarcaUseCase;
import com.taller.gestion_taller.application.usecases.marca.RegistrarMarca;
import com.taller.gestion_taller.application.usecases.marca.RegistrarMarcaUseCase;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.service.MarcaValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarcaBeanConfiguration {

    @Bean
    public MarcaValidator marcaValidator(MarcaRepository marcaRepository) {
        return new MarcaValidator(marcaRepository);
    }

    @Bean
    public RegistrarMarca registrarMarcaUseCase(MarcaApplicationMapper marcaApplicationMapper,
                                                MarcaValidator marcaValidator,
                                                MarcaRepository marcaRepository) {
        return new RegistrarMarcaUseCase(marcaApplicationMapper, marcaValidator, marcaRepository);
    }

    @Bean
    public ModificarMarca modificarMarcaUseCase(MarcaRepository marcaRepository,
                                                MarcaValidator marcaValidator) {
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
}
