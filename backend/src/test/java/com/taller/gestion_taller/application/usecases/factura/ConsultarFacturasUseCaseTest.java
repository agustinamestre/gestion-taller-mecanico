package com.taller.gestion_taller.application.usecases.factura;

import com.taller.gestion_taller.application.command.factura.ConsultarFacturasCommand;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
                .build();

        Factura factura = Factura.builder().id(1L).numeroFactura("F00000001").build();

        when(facturaRepository.findByFiltros(
                command.getId(), command.getNumeroFactura(), command.getClienteDni()))
                .thenReturn(List.of(factura));

        List<Factura> resultado = useCase.consultar(command);

        assertThat(resultado).containsExactly(factura);
    }

    @Test
    @DisplayName("Debe retornar lista vacia cuando no hay facturas que coincidan")
    void debeRetornarListaVaciaSiNoHayCoincidencias() {
        ConsultarFacturasCommand command = ConsultarFacturasCommand.builder().build();

        when(facturaRepository.findByFiltros(null, null, null))
                .thenReturn(Collections.emptyList());

        List<Factura> resultado = useCase.consultar(command);

        assertThat(resultado).isEmpty();
    }
}
