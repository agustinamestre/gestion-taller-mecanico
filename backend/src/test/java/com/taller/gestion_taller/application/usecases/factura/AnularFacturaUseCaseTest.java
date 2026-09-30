package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.AnularFacturaCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.service.FacturaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnularFacturaUseCase")
class AnularFacturaUseCaseTest {

    private static final Long FACTURA_ID = 4L;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private FacturaValidator facturaValidator;

    @InjectMocks
    private AnularFacturaUseCase useCase;

    @Test
    @DisplayName("Debe anular la factura")
    void debeAnularFacturaExitosamente() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(10L)
                .estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        Factura factura = Factura.builder()
                .id(FACTURA_ID)
                .ordenTrabajo(orden)
                .formaPago(FormaPago.EFECTIVO)
                .numeroFactura("F00000001")
                .estado(EstadoFactura.EMITIDA)
                .build();

        AnularFacturaCommand command = new AnularFacturaCommand(FACTURA_ID, "Error en la forma de pago");

        when(facturaRepository.findById(FACTURA_ID)).thenReturn(Optional.of(factura));
        when(facturaRepository.findByFiltros(null, null, null, null, orden.getId(), null, null))
                .thenReturn(List.of(factura));
        when(facturaRepository.save(any(Factura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Factura resultado = useCase.anularFactura(command);

        assertThat(resultado.getEstado()).isEqualTo(EstadoFactura.ANULADA);
        assertThat(resultado.getMotivoAnulacion()).isEqualTo("Error en la forma de pago");

        verify(facturaValidator).validarAnulacion(eq(factura), anyList());
        verify(facturaRepository).save(factura);
    }

    @Test
    @DisplayName("Lanzar excepcion cuando la factura no existe")
    void debeLanzarExcepcionCuandoFacturaNoExiste() {
        AnularFacturaCommand command = new AnularFacturaCommand(FACTURA_ID, "Motivo");

        when(facturaRepository.findById(FACTURA_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.anularFactura(command));

        verify(facturaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando la factura ya esta anulada")
    void debeLanzarExcepcionCuandoFacturaYaAnulada() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(10L)
                .estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        Factura factura = Factura.builder()
                .id(FACTURA_ID)
                .ordenTrabajo(orden)
                .formaPago(FormaPago.EFECTIVO)
                .numeroFactura("F00000001")
                .estado(EstadoFactura.ANULADA)
                .motivoAnulacion("Motivo anterior")
                .build();

        AnularFacturaCommand command = new AnularFacturaCommand(FACTURA_ID, "Nuevo motivo");

        when(facturaRepository.findById(FACTURA_ID)).thenReturn(Optional.of(factura));
        when(facturaRepository.findByFiltros(null, null, null, null, orden.getId(), null, null))
                .thenReturn(List.of(factura));

        assertThrows(BusinessRunTimeException.class, () -> useCase.anularFactura(command));

        verify(facturaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el motivo de anulacion esta vacio o en blanco")
    void debeLanzarExcepcionCuandoMotivoEstaEnBlanco() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(10L)
                .estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        Factura factura = Factura.builder()
                .id(FACTURA_ID)
                .ordenTrabajo(orden)
                .formaPago(FormaPago.EFECTIVO)
                .numeroFactura("F00000001")
                .estado(EstadoFactura.EMITIDA)
                .build();

        AnularFacturaCommand command = new AnularFacturaCommand(FACTURA_ID, "   ");

        when(facturaRepository.findById(FACTURA_ID)).thenReturn(Optional.of(factura));
        when(facturaRepository.findByFiltros(null, null, null, null, orden.getId(), null, null))
                .thenReturn(List.of(factura));

        assertThrows(BusinessRunTimeException.class, () -> useCase.anularFactura(command));

        verify(facturaRepository, never()).save(any());
    }
}