package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.presupuesto.EliminarItemPresupuestoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.ItemPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EliminarItemPresupuestoUseCaseTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @InjectMocks
    private EliminarItemPresupuestoUseCase eliminarItemPresupuestoUseCase;

    @Test
    @DisplayName("Debe eliminar el item cuando el presupuesto este pendiente y el item existe")
    public void debeEliminarItemCuandoElPresupuestoEstaPendienteYElItemExiste() {
        Long presupuestoId = 1L;
        Long itemId = 10L;

        ItemPresupuesto item = ItemPresupuesto.builder().id(itemId).build();
        Presupuesto presupuesto = Presupuesto.builder()
                .id(presupuestoId)
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>(List.of(item)))
                .build();

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.of(presupuesto));
        when(presupuestoRepository.save(presupuesto)).thenReturn(presupuesto);

        eliminarItemPresupuestoUseCase.eliminar(
                new EliminarItemPresupuestoCommand(presupuestoId, itemId));

        verify(presupuestoRepository).save(presupuesto);
    }

    @Test
    @DisplayName("Debe retornar NotFoundException cuando el presupuesto no existe")
    public void debeRtornarNotFoundExceptionCuandoElPresupuestoNoExiste() {
        Long presupuestoId = 99L;

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                eliminarItemPresupuestoUseCase.eliminar(
                        new EliminarItemPresupuestoCommand(presupuestoId, 1L)));

        assertEquals("PRESUPUESTO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar BusinessException cuando el presupuesto no esta en estado PENDIENTE")
    public void debeRetornarBusinessExceptionCuandoElPresupuestoNoEsPendiente() {
        Long presupuestoId = 1L;

        Presupuesto presupuesto = Presupuesto.builder()
                .id(presupuestoId)
                .estado(EstadoPresupuesto.APROBADO)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.of(presupuesto));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () ->
                eliminarItemPresupuestoUseCase.eliminar(
                        new EliminarItemPresupuestoCommand(presupuestoId, 1L)));

        assertEquals("PRESUPUESTO_NO_PENDIENTE", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar NotFoundException cuando el item no existe")
    public void debeRetornarNotFoundExceptionCuandoElItemNoExiste() {
        Long presupuestoId = 1L;
        Long itemId = 99L;

        Presupuesto presupuesto = Presupuesto.builder()
                .id(presupuestoId)
                .estado(EstadoPresupuesto.PENDIENTE)
                .items(new ArrayList<>())
                .build();

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.of(presupuesto));

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                eliminarItemPresupuestoUseCase.eliminar(
                        new EliminarItemPresupuestoCommand(presupuestoId, itemId)));

        assertEquals("ITEM_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }
}