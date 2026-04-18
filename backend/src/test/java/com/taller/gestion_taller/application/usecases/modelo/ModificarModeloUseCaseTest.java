package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.application.command.ModificarModeloCommand;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModificarModeloUseCase")
class ModificarModeloUseCaseTest {

    private static final Long ID = 1L;
    private static final String NUEVO_NOMBRE = "EcoSport";
    private static final Long NUEVA_MARCA_ID = 2L;
    private static final ModificarModeloCommand COMMAND = new ModificarModeloCommand(NUEVO_NOMBRE, NUEVA_MARCA_ID);

    @Mock private ModeloRepository modeloRepository;
    @Mock private MarcaRepository marcaRepository;
    @Mock private ModeloValidator modeloValidator;

    @InjectMocks
    private ModificarModeloUseCase useCase;

    @Test
    @DisplayName("debe modificar un modelo exitosamente")
    void debeModificarModeloExitosamente() {
        Modelo modeloExistente = mock(Modelo.class);
        when(modeloRepository.findById(ID)).thenReturn(Optional.of(modeloExistente));

        Marca nuevaMarca = mock(Marca.class);
        when(marcaRepository.findById(NUEVA_MARCA_ID)).thenReturn(Optional.of(nuevaMarca));

        Modelo modeloActualizado = mock(Modelo.class);
        when(modeloExistente.actualizar(NUEVO_NOMBRE, nuevaMarca)).thenReturn(modeloActualizado);
        when(modeloRepository.save(modeloActualizado)).thenReturn(modeloActualizado);

        Modelo resultado = useCase.modificar(ID, COMMAND);

        assertThat(resultado).isEqualTo(modeloActualizado);
        verify(modeloValidator).validarUnico(NUEVO_NOMBRE, NUEVA_MARCA_ID, ID);
        verify(modeloRepository).save(modeloActualizado);
    }

    @Test
    @DisplayName("debe lanzar excepcion si el modelo no existe")
    void debeLanzarExcepcionSiModeloNoExiste() {
        when(modeloRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.modificar(ID, COMMAND))
                .isInstanceOf(NotFoundException.class);
    }
}
