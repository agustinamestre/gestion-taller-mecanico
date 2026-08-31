package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.ConsultarFacturasCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConsultarFacturasUseCase")
class ConsultarFacturasUseCaseTest {

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private ConsultarFacturasUseCase useCase;

    @Test
    @DisplayName("Debe retornar las facturas que cumplen los filtros")
    void debeRetornarFacturasSegunFiltros() {
        ConsultarFacturasCommand command = ConsultarFacturasCommand.builder()
                .clienteDni("12345678")
                .fechaDesde(LocalDate.of(2026, 1, 1))
                .fechaHasta(LocalDate.of(2026, 8, 31))
                .build();

        Factura factura = Factura.builder().id(1L).numeroFactura("F00000001").build();

        when(facturaRepository.findByFiltros(
                command.getId(), command.getNumeroFactura(), command.getClienteDni(),
                command.getFechaDesde(), command.getFechaHasta()))
                .thenReturn(List.of(factura));

        List<Factura> resultado = useCase.consultar(command);

        assertThat(resultado).containsExactly(factura);
    }

    @Test
    @DisplayName("Debe retornar lista vacia cuando no hay facturas que coincidan")
    void debeRetornarListaVaciaSiNoHayCoincidencias() {
        ConsultarFacturasCommand command = ConsultarFacturasCommand.builder().build();

        when(facturaRepository.findByFiltros(null, null, null, null, null))
                .thenReturn(Collections.emptyList());

        List<Factura> resultado = useCase.consultar(command);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Lanzar excepcion cuando la fecha desde es posterior a la fecha hasta")
    void debeLanzarExcepcionCuandoRangoFechasEsInvalido() {
        LocalDate desde = LocalDate.of(2026, 8, 31);
        LocalDate hasta = LocalDate.of(2026, 1, 1);

        ConsultarFacturasCommand command = ConsultarFacturasCommand.builder()
                .fechaDesde(desde)
                .fechaHasta(hasta)
                .build();

        Exception exception = assertThrows(BusinessRunTimeException.class, () -> {
            useCase.consultar(command);
        });

        assertTrue(exception.getMessage().contains(
                "La fecha de inicio (" + desde + ") no puede ser posterior a la fecha de fin (" + hasta + ")"));
        verify(facturaRepository, never()).findByFiltros(any(), any(), any(), any(), any());
    }
}
