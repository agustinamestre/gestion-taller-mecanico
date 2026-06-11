package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObtenerOrdenTrabajoPorIdUseCaseTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @InjectMocks
    private ObtenerOrdenTrabajoPorIdUseCase obtenerOrdenTrabajoPorIdUseCase;

    private OrdenTrabajo ordenTrabajo;

    @BeforeEach
    void setUp() {
        ordenTrabajo = OrdenTrabajo.builder().id(1L).build();
    }

    @Test
    void obtenerOrdenPorIdExitosamente() {
        // Arrange
        Long idExistente = 1L;
        when(ordenTrabajoRepository.findById(idExistente)).thenReturn(Optional.of(ordenTrabajo));

        // Act
        OrdenTrabajo resultado = obtenerOrdenTrabajoPorIdUseCase.obtener(idExistente);

        // Assert
        assertNotNull(resultado);
        assertEquals(idExistente, resultado.getId());
    }

    @Test
    void obtenerOrdenPorIdLanzaNotFoundException() {
        // Arrange
        Long idInexistente = 99L;
        when(ordenTrabajoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            obtenerOrdenTrabajoPorIdUseCase.obtener(idInexistente);
        });
    }
}
