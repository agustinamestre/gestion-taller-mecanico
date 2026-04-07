package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.application.command.RegistrarMarcaCommand;
import com.taller.gestion_taller.application.mapper.MarcaApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import com.taller.gestion_taller.domain.service.MarcaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarMarcaUseCase")
class RegistrarMarcaUseCaseTest {

    private static final String NOMBRE = "Ford";
    private static final RegistrarMarcaCommand COMMAND = new RegistrarMarcaCommand(NOMBRE);

    @Mock private MarcaApplicationMapper marcaApplicationMapper;
    @Mock private MarcaValidator marcaValidator;
    @Mock private MarcaRepository marcaRepository;

    @InjectMocks
    private RegistrarMarcaUseCase useCase;

    @Test
    @DisplayName("debe retornar la marca persistida")
    void debeRetornarMarcaPersistida() {
        Marca marcaGuardada = setupMarcaExitosa();

        Marca resultado = useCase.registrarMarca(COMMAND);

        assertThat(resultado).isEqualTo(marcaGuardada);
        verify(marcaApplicationMapper).commandToDomain(COMMAND);

    }

    @Test
    @DisplayName("debe lanzar BusinessRunTimeException")
    void debeLanzarExcepcion() {
        Marca marca = setupMarcaMapeada();
        doThrow(BusinessRunTimeException.class)
                .when(marcaValidator).validarNombreUnico(NOMBRE, null);

        assertThatThrownBy(() -> useCase.registrarMarca(COMMAND))
                .isInstanceOf(BusinessRunTimeException.class);
    }

    @Test
    @DisplayName("no debe persistir la marca duplicada")
    void noDebePersistirMarcaDuplicada() {
        setupMarcaMapeada();
        doThrow(BusinessRunTimeException.class)
                .when(marcaValidator).validarNombreUnico(NOMBRE, null);

        try { useCase.registrarMarca(COMMAND); } catch (BusinessRunTimeException ignored) {}

        verify(marcaRepository, never()).save(any());
    }

    private Marca setupMarcaMapeada() {
        Marca marca = mock(Marca.class);
        when(marca.getNombre()).thenReturn(NOMBRE);
        when(marcaApplicationMapper.commandToDomain(COMMAND)).thenReturn(marca);
        return marca;
    }

    private Marca setupMarcaExitosa() {
        Marca marca = setupMarcaMapeada();
        Marca marcaGuardada = mock(Marca.class);
        when(marcaRepository.save(marca)).thenReturn(marcaGuardada);
        return marcaGuardada;
    }
}