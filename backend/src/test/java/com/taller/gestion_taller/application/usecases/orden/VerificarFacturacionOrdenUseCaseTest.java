package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.ItemOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.TipoFactura;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("VerificarFacturacionOrdenUseCase")
class VerificarFacturacionOrdenUseCaseTest {

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private VerificarFacturacionOrdenUseCase useCase;

    private OrdenTrabajo ordenConTotal(Long id, BigDecimal total) {
        ItemOrdenTrabajo item = ItemOrdenTrabajo.builder()
                .cantidad(1)
                .precioUnitario(total)
                .build();
        return OrdenTrabajo.builder()
                .id(id)
                .items(List.of(item))
                .build();
    }

    private Factura facturaActiva(Long ordenId, BigDecimal monto) {
        return Factura.builder()
                .ordenTrabajo(OrdenTrabajo.builder().id(ordenId).build())
                .estado(EstadoFactura.EMITIDA)
                .tipoFactura(TipoFactura.SENIA)
                .montoFacturado(monto)
                .build();
    }

    @Test
    @DisplayName("estaTotalmenteFacturada devuelve false cuando la orden no tiene facturas")
    void estaTotalmenteFacturadaSinFacturas() {
        OrdenTrabajo orden = ordenConTotal(1L, BigDecimal.valueOf(1000));

        when(facturaRepository.findActivasByOrdenTrabajoId(1L)).thenReturn(List.of());

        assertThat(useCase.estaTotalmenteFacturada(orden)).isFalse();
    }

    @Test
    @DisplayName("estaTotalmenteFacturada devuelve false cuando el total de la orden es cero, aunque no haya facturas activas")
    void estaTotalmenteFacturadaConTotalCero() {
        OrdenTrabajo orden = ordenConTotal(1L, BigDecimal.ZERO);

        when(facturaRepository.findActivasByOrdenTrabajoId(1L)).thenReturn(List.of());

        assertThat(useCase.estaTotalmenteFacturada(orden)).isFalse();
    }

    @Test
    @DisplayName("estaTotalmenteFacturada devuelve false cuando las facturas activas suman menos que el total")
    void estaTotalmenteFacturadaConSaldoPendiente() {
        OrdenTrabajo orden = ordenConTotal(1L, BigDecimal.valueOf(1000));

        when(facturaRepository.findActivasByOrdenTrabajoId(1L))
                .thenReturn(List.of(facturaActiva(1L, BigDecimal.valueOf(300))));

        assertThat(useCase.estaTotalmenteFacturada(orden)).isFalse();
    }

    @Test
    @DisplayName("estaTotalmenteFacturada devuelve true cuando las facturas activas suman exactamente el total")
    void estaTotalmenteFacturadaConTotalCubierto() {
        OrdenTrabajo orden = ordenConTotal(1L, BigDecimal.valueOf(1000));

        when(facturaRepository.findActivasByOrdenTrabajoId(1L))
                .thenReturn(List.of(facturaActiva(1L, BigDecimal.valueOf(1000))));

        assertThat(useCase.estaTotalmenteFacturada(orden)).isTrue();
    }

    @Test
    @DisplayName("estaTotalmenteFacturada devuelve false cuando las unicas facturas de la orden estan anuladas")
    void estaTotalmenteFacturadaConSoloFacturasAnuladas() {
        OrdenTrabajo orden = ordenConTotal(1L, BigDecimal.valueOf(1000));

        when(facturaRepository.findActivasByOrdenTrabajoId(1L)).thenReturn(List.of());

        assertThat(useCase.estaTotalmenteFacturada(orden)).isFalse();
    }

    @Test
    @DisplayName("estanTotalmenteFacturadas evalua correctamente un lote de ordenes con estados mixtos")
    void estanTotalmenteFacturadasConOrdenesMixtas() {
        OrdenTrabajo ordenSinFacturas = ordenConTotal(1L, BigDecimal.valueOf(1000));
        OrdenTrabajo ordenTotalmenteFacturada = ordenConTotal(2L, BigDecimal.valueOf(1000));
        OrdenTrabajo ordenParcialmenteFacturada = ordenConTotal(3L, BigDecimal.valueOf(500));

        Factura facturaOrden2 = facturaActiva(2L, BigDecimal.valueOf(1000));
        Factura facturaOrden3 = facturaActiva(3L, BigDecimal.valueOf(200));

        List<OrdenTrabajo> ordenes = List.of(ordenSinFacturas, ordenTotalmenteFacturada, ordenParcialmenteFacturada);

        when(facturaRepository.findActivasByOrdenTrabajoIds(List.of(1L, 2L, 3L)))
                .thenReturn(List.of(facturaOrden2, facturaOrden3));

        Map<Long, Boolean> resultado = useCase.estanTotalmenteFacturadas(ordenes);

        assertThat(resultado)
                .containsEntry(1L, false)
                .containsEntry(2L, true)
                .containsEntry(3L, false);
    }
}
