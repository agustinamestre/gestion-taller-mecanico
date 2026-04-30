package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.ModeloApplicationMapper;
import com.taller.gestion_taller.application.usecases.modelo.DesactivarModelo;
import com.taller.gestion_taller.application.usecases.modelo.DesactivarModeloUseCase;
import com.taller.gestion_taller.application.usecases.modelo.ListarModelos;
import com.taller.gestion_taller.application.usecases.modelo.ListarModelosUseCase;
import com.taller.gestion_taller.application.usecases.modelo.ModificarModelo;
import com.taller.gestion_taller.application.usecases.modelo.ModificarModeloUseCase;
import com.taller.gestion_taller.application.usecases.modelo.RegistrarModelo;
import com.taller.gestion_taller.application.usecases.modelo.RegistrarModeloUseCase;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.service.ModeloValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModeloBeanConfiguration {

    @Bean
    public ModeloValidator modeloValidator(ModeloRepository modeloRepository) {
        return new ModeloValidator(modeloRepository);
    }

    @Bean
    public RegistrarModelo registrarModeloUseCase(ModeloRepository modeloRepository,
                                                  MarcaRepository marcaRepository,
                                                  ModeloApplicationMapper modeloApplicationMapper,
                                                  ModeloValidator modeloValidator) {
        return new RegistrarModeloUseCase(modeloRepository, marcaRepository, modeloApplicationMapper, modeloValidator);
    }

    @Bean
    public ListarModelos listarModelosUseCase(ModeloRepository modeloRepository) {
        return new ListarModelosUseCase(modeloRepository);
    }

    @Bean
    public ModificarModelo modificarModeloUseCase(ModeloRepository modeloRepository,
                                                  MarcaRepository marcaRepository,
                                                  ModeloValidator modeloValidator) {
        return new ModificarModeloUseCase(modeloRepository, marcaRepository, modeloValidator);
    }

    @Bean
    public DesactivarModelo desactivarModeloUseCase(ModeloRepository modeloRepository) {
        return new DesactivarModeloUseCase(modeloRepository);
    }
}
