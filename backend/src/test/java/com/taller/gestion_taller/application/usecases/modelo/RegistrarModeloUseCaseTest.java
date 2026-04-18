package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.application.command.RegistrarModeloCommand;
import com.taller.gestion_taller.application.mapper.ModeloApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.service.ModeloValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarModeloUseCase")
class RegistrarModeloUseCaseTest {

    private static final String NOMBRE = "Fiesta";
    private static final Long MARCA_ID = 1L;
    private static final RegistrarModeloCommand COMMAND = new RegistrarModeloCommand(NOMBRE, MARCA_ID);

    @Mock private ModeloRepository modeloRepository;
    @Mock private MarcaRepository marcaRepository;
    @Mock private ModeloApplicationMapper modeloApplicationMapper;
    @Mock private ModeloValidator modeloValidator;

    @InjectMocks
    private RegistrarModeloUseCase useCase;

    @Test
    @DisplayName("debe registrar un modelo exitosamente")
    void debeRegistrarModeloExitosamente() {
        Marca marca = mock(Marca.class);
        when(marcaRepository.findById(MARCA_ID)).thenReturn(Optional.of(marca));
        
        Modelo modelo = mock(Modelo.class);
        when(modeloApplicationMapper.commandToDomain(COMMAND, marca)).thenReturn(modelo);
        when(modeloRepository.save(modelo)).thenReturn(modelo);

        Modelo resultado = useCase.registrar(COMMAND);

        assertThat(resultado).isEqualTo(modelo);
        verify(modeloValidator).validarUnico(NOMBRE, MARCA_ID, null);
        verify(modeloRepository).save(modelo);
    }

    @Test
    @DisplayName("debe lanzar excepcion si la marca no existe")
    void debeLanzarExcepcionSiMarcaNoExiste() {
        when(marcaRepository.findById(MARCA_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.registrar(COMMAND);
        });

        assertTrue(exception.getMessage().contains("No se encontro la marca ingresada."));

        verify(modeloRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si el modelo esta duplicado")
    void debeLanzarExcepcionSiModeloDuplicado() {
        Marca marca = mock(Marca.class);
        when(marcaRepository.findById(MARCA_ID)).thenReturn(Optional.of(marca));
        doThrow(BusinessRunTimeException.class)
                .when(modeloValidator).validarUnico(NOMBRE, MARCA_ID, null);

        assertThatThrownBy(() -> useCase.registrar(COMMAND))
                .isInstanceOf(BusinessRunTimeException.class);

        verify(modeloRepository, never()).save(any());
    }
}
