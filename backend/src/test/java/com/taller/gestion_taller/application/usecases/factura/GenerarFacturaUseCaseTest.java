package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.SituacionIva;
import com.taller.gestion_taller.domain.model.TipoComprobante;
import com.taller.gestion_taller.domain.model.TipoFactura;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ContadorFacturaRepository;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.service.FacturaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenerarFacturaUseCase")
class GenerarFacturaUseCaseTest {

    private static final Long ORDEN_ID = 10L;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private FacturaValidator facturaValidator;

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Mock
    private ContadorFacturaRepository contadorFacturaRepository;

    @InjectMocks
    private GenerarFacturaUseCase useCase;

    @Test
    @DisplayName("Debe generar la factura y asignarle un numero a partir del contador dedicado")
    void debeGenerarFacturaExitosamente() {
        Cliente cliente = Cliente.builder().dni("12345678").situacionIva(SituacionIva.CONSUMIDOR_FINAL).build();
        Vehiculo vehiculo = Vehiculo.builder().cliente(cliente).build();
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(ORDEN_ID)
                .vehiculo(vehiculo)
                .estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        GenerarFacturaCommand command = new GenerarFacturaCommand(ORDEN_ID, FormaPago.EFECTIVO, TipoFactura.FINAL, BigDecimal.TEN);

        when(ordenTrabajoRepository.findById(ORDEN_ID)).thenReturn(Optional.of(orden));
        when(facturaRepository.findActivasByOrdenTrabajoId(ORDEN_ID)).thenReturn(List.of());
        when(contadorFacturaRepository.siguienteNumero()).thenReturn(1L);
        when(facturaRepository.save(any(Factura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Factura resultado = useCase.generarFactura(command);

        assertThat(resultado.getNumeroFactura()).isEqualTo("F00000001");
        assertThat(resultado.getOrdenTrabajo()).isEqualTo(orden);
        assertThat(resultado.getTipoComprobante()).isEqualTo(TipoComprobante.C);
        assertThat(resultado.getTipoFactura()).isEqualTo(TipoFactura.FINAL);
        assertThat(resultado.getMontoFacturado()).isEqualTo(BigDecimal.TEN);

        verify(facturaValidator).validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.TEN, List.of());

        ArgumentCaptor<Factura> facturaCaptor = ArgumentCaptor.forClass(Factura.class);
        verify(facturaRepository).save(facturaCaptor.capture());
        assertThat(facturaCaptor.getValue().getNumeroFactura()).isEqualTo("F00000001");
    }

    @Test
    @DisplayName("Lanzar excepcion cuando la orden de trabajo no existe")
    void debeLanzarExcepcionCuandoOrdenNoExiste() {
        GenerarFacturaCommand command = new GenerarFacturaCommand(ORDEN_ID, FormaPago.EFECTIVO, TipoFactura.FINAL, BigDecimal.TEN);

        when(ordenTrabajoRepository.findById(ORDEN_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.generarFactura(command);
        });

        assertTrue(exception.getMessage().contains("No se encontro la orden de trabajo con ID: " + ORDEN_ID));
        verify(facturaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Propaga la excepcion de negocio cuando el validador rechaza la factura")
    void debePropagarExcepcionDelValidador() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(ORDEN_ID)
                .estado(EstadoOrdenTrabajo.INGRESADO)
                .build();

        GenerarFacturaCommand command = new GenerarFacturaCommand(ORDEN_ID, FormaPago.EFECTIVO, TipoFactura.FINAL, BigDecimal.TEN);

        when(ordenTrabajoRepository.findById(ORDEN_ID)).thenReturn(Optional.of(orden));
        when(facturaRepository.findActivasByOrdenTrabajoId(ORDEN_ID)).thenReturn(List.of());
        doThrow(new BusinessRunTimeException(
                com.taller.gestion_taller.domain.exception.BusinessErrors.ordenNoFacturable(orden.getEstado())))
                .when(facturaValidator).validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.TEN, List.of());

        assertThrows(BusinessRunTimeException.class, () -> useCase.generarFactura(command));

        verify(facturaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar la factura final por el saldo restante cuando ya existe una seña emitida para la orden")
    void debeGenerarFacturaFinalLuegoDeUnaSeniaEmitida() {
        Cliente cliente = Cliente.builder().dni("12345678").situacionIva(SituacionIva.CONSUMIDOR_FINAL).build();
        Vehiculo vehiculo = Vehiculo.builder().cliente(cliente).build();
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(ORDEN_ID)
                .vehiculo(vehiculo)
                .estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        Factura seniaEmitida = Factura.builder()
                .tipoFactura(TipoFactura.SENIA)
                .estado(EstadoFactura.EMITIDA)
                .montoFacturado(BigDecimal.valueOf(300))
                .build();

        GenerarFacturaCommand command = new GenerarFacturaCommand(ORDEN_ID, FormaPago.EFECTIVO, TipoFactura.FINAL, BigDecimal.valueOf(700));

        when(ordenTrabajoRepository.findById(ORDEN_ID)).thenReturn(Optional.of(orden));
        when(facturaRepository.findActivasByOrdenTrabajoId(ORDEN_ID)).thenReturn(List.of(seniaEmitida));
        when(contadorFacturaRepository.siguienteNumero()).thenReturn(2L);
        when(facturaRepository.save(any(Factura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Factura resultado = useCase.generarFactura(command);

        assertThat(resultado.getTipoFactura()).isEqualTo(TipoFactura.FINAL);
        assertThat(resultado.getMontoFacturado()).isEqualTo(BigDecimal.valueOf(700));
        assertThat(resultado.getNumeroFactura()).isEqualTo("F00000002");

        verify(facturaValidator).validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.valueOf(700), List.of(seniaEmitida));
    }

    @Test
    @DisplayName("Propaga la excepcion de negocio cuando el validador rechaza una segunda factura final duplicada")
    void debePropagarExcepcionDeFacturaFinalDuplicada() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(ORDEN_ID)
                .estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        Factura finalYaEmitida = Factura.builder()
                .tipoFactura(TipoFactura.FINAL)
                .estado(EstadoFactura.EMITIDA)
                .montoFacturado(BigDecimal.TEN)
                .build();

        GenerarFacturaCommand command = new GenerarFacturaCommand(ORDEN_ID, FormaPago.EFECTIVO, TipoFactura.FINAL, BigDecimal.TEN);

        when(ordenTrabajoRepository.findById(ORDEN_ID)).thenReturn(Optional.of(orden));
        when(facturaRepository.findActivasByOrdenTrabajoId(ORDEN_ID)).thenReturn(List.of(finalYaEmitida));
        doThrow(new BusinessRunTimeException(BusinessErrors.ordenYaTieneFacturaDeTipo(orden.getId(), TipoFactura.FINAL)))
                .when(facturaValidator)
                .validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.TEN, List.of(finalYaEmitida));

        assertThrows(BusinessRunTimeException.class, () -> useCase.generarFactura(command));

        verify(facturaRepository, never()).save(any());
    }
}
