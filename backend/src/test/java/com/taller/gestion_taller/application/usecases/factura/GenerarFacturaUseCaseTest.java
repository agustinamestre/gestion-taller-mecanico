package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.GenerarFacturaCommand;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.service.FacturaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private GenerarFacturaUseCase useCase;

    @Test
    @DisplayName("Debe generar la factura y asignarle un numero a partir del id guardado")
    void debeGenerarFacturaExitosamente() {
        OrdenTrabajo orden = OrdenTrabajo.builder()
                .id(ORDEN_ID)
                .estado(EstadoOrdenTrabajo.FINALIZADO)
                .build();

        GenerarFacturaCommand command = new GenerarFacturaCommand(ORDEN_ID, FormaPago.EFECTIVO);

        Factura guardada = Factura.builder()
                .id(1L)
                .ordenTrabajo(orden)
                .formaPago(FormaPago.EFECTIVO)
                .build();

        Factura facturaConNumero = guardada.toBuilder()
                .numeroFactura("F00000001")
                .build();

        when(ordenTrabajoRepository.findById(ORDEN_ID)).thenReturn(Optional.of(orden));
        when(facturaRepository.save(any(Factura.class))).thenReturn(guardada);
        when(facturaRepository.actualizarNumeroFactura(1L, "F00000001")).thenReturn(facturaConNumero);

        Factura resultado = useCase.generarFactura(command);

        assertThat(resultado).isEqualTo(facturaConNumero);
        assertThat(resultado.getNumeroFactura()).isEqualTo("F00000001");
        verify(facturaValidator).validarOrdenParaFacturacion(orden);
        verify(facturaRepository).actualizarNumeroFactura(1L, "F00000001");
    }

    @Test
    @DisplayName("Lanzar excepcion cuando la orden de trabajo no existe")
    void debeLanzarExcepcionCuandoOrdenNoExiste() {
        GenerarFacturaCommand command = new GenerarFacturaCommand(ORDEN_ID, FormaPago.EFECTIVO);

        when(ordenTrabajoRepository.findById(ORDEN_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.generarFactura(command);
        });

        assertTrue(exception.getMessage().contains("No se encontro la orden de trabajo con ID: " + ORDEN_ID));
        verify(facturaRepository, never()).save(any());
    }
}
