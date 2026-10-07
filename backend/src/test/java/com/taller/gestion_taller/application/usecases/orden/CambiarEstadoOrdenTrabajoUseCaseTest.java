package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.ItemOrdenTrabajo;
import com.taller.gestion_taller.domain.model.TipoFactura;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CambiarEstadoOrdenTrabajoUseCase")
class CambiarEstadoOrdenTrabajoUseCaseTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private CambiarEstadoOrdenTrabajoUseCase useCase;

    @Test
    @DisplayName("debe cambiar estado exitosamente cuando la transicion es valida")
    void debeCambiarEstadoExitosamente() {
        OrdenTrabajo orden = mock(OrdenTrabajo.class);
        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.EN_REPARACION));

        verify(orden).cambiarEstado(EstadoOrdenTrabajo.EN_REPARACION);
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe setear fechaEgreso al pasar a FINALIZADO")
    void debeSetearFechaEgresoAlFinalizar() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L)
                .estado(EstadoOrdenTrabajo.EN_REPARACION)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.FINALIZADO));

        assertThat(orden.getFechaEgreso()).isEqualTo(LocalDate.now());
        verify(ordenTrabajoRepository).save(orden);
    }

    @Test
    @DisplayName("debe lanzar excepcion si la orden no existe")
    void debeLanzarExcepcionSiOrdenNoExiste() {
        when(ordenTrabajoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(99L, EstadoOrdenTrabajo.EN_REPARACION)))
                .isInstanceOf(NotFoundException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si la transicion es invalida")
    void debeLanzarExcepcionSiTransicionInvalida() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L)
                .estado(EstadoOrdenTrabajo.ENTREGADO)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        assertThatThrownBy(() ->
                useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.EN_REPARACION)))
                .isInstanceOf(BusinessRunTimeException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar excepcion si CANCELADO intenta cambiar de estado")
    void debeLanzarExcepcionSiCanceladoIntentaCambiar() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L)
                .estado(EstadoOrdenTrabajo.CANCELADO)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        assertThatThrownBy(() ->
                useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.EN_REPARACION)))
                .isInstanceOf(BusinessRunTimeException.class);

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("al entregar una orden que incluye service debe registrar el service en el vehiculo")
    void debeRegistrarServiceAlEntregarOrdenConService() {
        Vehiculo vehiculo = Vehiculo.builder().id(5L).kilometrajeActual(48000)
                .fechaUltimoService(LocalDate.now().minusMonths(14)).kmUltimoService(30000).build();
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L).vehiculo(vehiculo).estado(EstadoOrdenTrabajo.FINALIZADO).incluyeService(true)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(vehiculoRepository.findById(5L)).thenReturn(Optional.of(vehiculo));

        useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.ENTREGADO));

        ArgumentCaptor<Vehiculo> captor = ArgumentCaptor.forClass(Vehiculo.class);
        verify(vehiculoRepository).save(captor.capture());
        assertThat(captor.getValue().getFechaUltimoService()).isEqualTo(LocalDate.now());
        assertThat(captor.getValue().getKmUltimoService()).isEqualTo(48000);
    }

    @Test
    @DisplayName("no permite entregar una orden con saldo pendiente")
    void noDebeEntregarOrdenConSaldoPendiente() {
        ItemOrdenTrabajo item = ItemOrdenTrabajo.builder().cantidad(1).precioUnitario(BigDecimal.valueOf(1000)).build();
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L).vehiculo(Vehiculo.builder().id(5L).build()).estado(EstadoOrdenTrabajo.FINALIZADO)
                .items(List.of(item))
                .build();
        Factura senia = Factura.builder().tipoFactura(TipoFactura.SENIA).estado(EstadoFactura.EMITIDA)
                .montoFacturado(BigDecimal.valueOf(600)).build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(facturaRepository.findActivasByOrdenTrabajoId(1L)).thenReturn(List.of(senia));

        assertThatThrownBy(() ->
                useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.ENTREGADO)))
                .isInstanceOf(BusinessRunTimeException.class)
                .extracting("businessError.code")
                .isEqualTo("ORDEN_CON_SALDO_PENDIENTE");

        verify(ordenTrabajoRepository, never()).save(any());
    }

    @Test
    @DisplayName("al entregar una orden que no incluye service no debe tocar el vehiculo")
    void noDebeRegistrarServiceSiLaOrdenNoLoIncluye() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(1L).vehiculo(Vehiculo.builder().id(5L).build()).estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        when(ordenTrabajoRepository.findById(1L)).thenReturn(Optional.of(orden));

        useCase.cambiar(new CambiarEstadoOrdenTrabajoCommand(1L, EstadoOrdenTrabajo.ENTREGADO));

        verifyNoInteractions(vehiculoRepository);
    }
}
