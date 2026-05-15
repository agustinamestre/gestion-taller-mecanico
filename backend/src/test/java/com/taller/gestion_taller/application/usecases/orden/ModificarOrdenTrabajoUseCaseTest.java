package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.ModificarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModificarOrdenTrabajoUseCase")
class ModificarOrdenTrabajoUseCaseTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @InjectMocks
    private ModificarOrdenTrabajoUseCase useCase;

    @Test
    @DisplayName("debe modificar la descripcion exitosamente")
    void debeModificarDescripcionExitosamente() {
        OrdenTrabajo orden = mock(OrdenTrabajo.class);
        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(ordenTrabajoRepository.save(orden)).thenReturn(orden);

        OrdenTrabajo resultado = useCase.modificar(
                new ModificarOrdenTrabajoCommand(1L, "Nueva descripcion"));

        assertThat(resultado).isEqualTo(orden);
        verify(orden).modificar("Nueva descripcion");
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe lanzar excepcion si la orden no existe")
    void debeLanzarExcepcionSiOrdenNoExiste() {
        when(ordenTrabajoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.modificar(new ModificarOrdenTrabajoCommand(99L, "Nueva descripcion")))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }
}
