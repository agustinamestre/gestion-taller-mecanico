package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObtenerPresupuestoUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @InjectMocks
    private ObtenerPresupuestoUseCase obtenerPresupuestoUseCase;

    @Test
    public void testObtener_debeRetornarElPresupuestoCuandoExiste() {
        Long id = 1L;
        Presupuesto presupuesto = Presupuesto.builder().id(id).build();

        when(presupuestoRepository.findById(id)).thenReturn(Optional.of(presupuesto));

        Presupuesto result = obtenerPresupuestoUseCase.obtener(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testObtener_debeRetornarNotFoundException_cuandoNoExiste() {
        Long id = 99L;

        when(presupuestoRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                obtenerPresupuestoUseCase.obtener(id)
        );

        assertEquals("PRESUPUESTO_NO_ENCONTRADO", exception.getBusinessError().code());

    }
}