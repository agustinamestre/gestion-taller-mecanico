package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.EliminarItemOrdenTrabajoCommand;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EliminarItemOrdenTrabajoUseCase")
class EliminarItemOrdenTrabajoUseCaseTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;
    @InjectMocks
    private EliminarItemOrdenTrabajoUseCase useCase;

    @Test
    @DisplayName("debe eliminar item exitosamente")
    void debeEliminarItemExitosamente() {
        OrdenTrabajo orden = mock(OrdenTrabajo.class);
        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        useCase.eliminar(new EliminarItemOrdenTrabajoCommand(1L, 10L));

        verify(orden).eliminarItem(10L);
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe lanzar excepcion si la orden no existe")
    void debeLanzarExcepcionSiOrdenNoExiste() {
        when(ordenTrabajoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.eliminar(
                new EliminarItemOrdenTrabajoCommand(99L, 1L)))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }
}