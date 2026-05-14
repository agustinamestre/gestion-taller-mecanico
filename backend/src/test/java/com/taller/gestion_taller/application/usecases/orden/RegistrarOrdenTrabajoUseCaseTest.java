package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.application.mapper.OrdenTrabajoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarOrdenTrabajoUseCase")
class RegistrarOrdenTrabajoUseCaseTest {

    private static final String PATENTE = "ABC123";
    private static final Long USUARIO_ID = 1L;
    private static final Long PRESUPUESTO_ID = 1L;
    private static final String DESCRIPCION = "Ruido en el motor";

    private static final RegistrarOrdenTrabajoCommand COMMAND_CON_PRESUPUESTO = new RegistrarOrdenTrabajoCommand(
            PATENTE, PRESUPUESTO_ID, DESCRIPCION, USUARIO_ID);

    private static final RegistrarOrdenTrabajoCommand COMMAND_SIN_PRESUPUESTO = new RegistrarOrdenTrabajoCommand(
            PATENTE, null, DESCRIPCION, USUARIO_ID);

    @Mock private OrdenTrabajoRepository ordenTrabajoRepository;
    @Mock private VehiculoRepository vehiculoRepository;
    @Mock private PresupuestoRepository presupuestoRepository;
    @Mock private OrdenTrabajoApplicationMapper ordenTrabajoApplicationMapper;

    @InjectMocks
    private RegistrarOrdenTrabajoUseCase useCase;

    @Test
    @DisplayName("debe registrar una orden con presupuesto aprobado exitosamente")
    void debeRegistrarOrdenConPresupuestoExitosamente() {
        Vehiculo vehiculo = mock(Vehiculo.class);
        Presupuesto presupuesto = mock(Presupuesto.class);
        OrdenTrabajo orden = mock(OrdenTrabajo.class);

        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
        when(presupuesto.getEstado()).thenReturn(EstadoPresupuesto.APROBADO);
        when(ordenTrabajoApplicationMapper.commandToDomain(COMMAND_CON_PRESUPUESTO)).thenReturn(orden);
        when(ordenTrabajoRepository.save(orden)).thenReturn(orden);

        OrdenTrabajo resultado = useCase.registrar(COMMAND_CON_PRESUPUESTO);

        assertThat(resultado).isEqualTo(orden);
        verify(orden).setVehiculo(vehiculo);
        verify(orden).setPresupuesto(presupuesto);
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe registrar una orden sin presupuesto exitosamente")
    void debeRegistrarOrdenSinPresupuestoExitosamente() {
        Vehiculo vehiculo = mock(Vehiculo.class);
        OrdenTrabajo orden = mock(OrdenTrabajo.class);

        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
        when(ordenTrabajoApplicationMapper.commandToDomain(COMMAND_SIN_PRESUPUESTO)).thenReturn(orden);
        when(ordenTrabajoRepository.save(orden)).thenReturn(orden);

        OrdenTrabajo resultado = useCase.registrar(COMMAND_SIN_PRESUPUESTO);

        assertThat(resultado).isEqualTo(orden);
        verify(orden).setVehiculo(vehiculo);
        verify(orden, never()).setPresupuesto(any());
        verify(presupuestoRepository, never()).findById(any());
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe lanzar excepcion si el vehiculo no existe")
    void debeLanzarExcepcionSiVehiculoNoExiste() {
        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si el presupuesto no existe")
    void debeLanzarExcepcionSiPresupuestoNoExiste() {
        Vehiculo vehiculo = mock(Vehiculo.class);
        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
        when(ordenTrabajoApplicationMapper.commandToDomain(COMMAND_CON_PRESUPUESTO)).thenReturn(mock(OrdenTrabajo.class));
        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si el presupuesto no esta aprobado")
    void debeLanzarExcepcionSiPresupuestoNoEstaAprobado() {
        Vehiculo vehiculo = mock(Vehiculo.class);
        Presupuesto presupuesto = mock(Presupuesto.class);

        when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
        when(ordenTrabajoApplicationMapper.commandToDomain(COMMAND_CON_PRESUPUESTO)).thenReturn(mock(OrdenTrabajo.class));
        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
        when(presupuesto.getEstado()).thenReturn(EstadoPresupuesto.PENDIENTE);

        assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
                .isInstanceOf(BusinessRunTimeException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }
}