package com.taller.gestion_taller.application.usecases.dashboard;

import com.taller.gestion_taller.application.dto.DashboardResumen;
import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObtenerResumenDashboardUseCase")
class ObtenerResumenDashboardUseCaseTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private ObtenerResumenDashboardUseCase useCase;

    private Presupuesto presupuestoConEstado(EstadoPresupuesto estado) {
        return Presupuesto.builder().id(1L).estado(estado).build();
    }

    private Factura facturaEmitida(BigDecimal monto) {
        return Factura.builder().estado(EstadoFactura.EMITIDA).montoFacturado(monto).build();
    }

    @Test
    @DisplayName("debe calcular correctamente todos los indicadores del resumen")
    void debeCalcularResumenCompleto() {
        when(ordenTrabajoRepository.findByFiltros(null, List.of(EstadoOrdenTrabajo.INGRESADO, EstadoOrdenTrabajo.EN_REPARACION)))
                .thenReturn(List.of(mock(com.taller.gestion_taller.domain.model.OrdenTrabajo.class),
                        mock(com.taller.gestion_taller.domain.model.OrdenTrabajo.class)));

        when(ordenTrabajoRepository.findByFiltros(null, List.of(EstadoOrdenTrabajo.FINALIZADO)))
                .thenReturn(List.of(mock(com.taller.gestion_taller.domain.model.OrdenTrabajo.class)));

        when(presupuestoRepository.findAll()).thenReturn(List.of(
                presupuestoConEstado(EstadoPresupuesto.PENDIENTE),
                presupuestoConEstado(EstadoPresupuesto.PENDIENTE),
                presupuestoConEstado(EstadoPresupuesto.APROBADO),
                presupuestoConEstado(EstadoPresupuesto.RECHAZADO)
        ));

        when(facturaRepository.findByFiltros(any(), any(), any(), any(), any(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(
                        facturaEmitida(BigDecimal.valueOf(1000)),
                        facturaEmitida(BigDecimal.valueOf(500))
                ));

        when(alertaRepository.countByContactadoFalse()).thenReturn(3L);

        DashboardResumen resumen = useCase.obtener();

        assertThat(resumen.ordenesEnCurso()).isEqualTo(2);
        assertThat(resumen.ordenesListasParaEntregar()).isEqualTo(1);
        assertThat(resumen.presupuestosPendientes()).isEqualTo(2);
        assertThat(resumen.presupuestosAprobadosSinOrden()).isEqualTo(1);
        assertThat(resumen.montoFacturadoUltimoMes()).isEqualTo(BigDecimal.valueOf(1500));
        assertThat(resumen.alertasPendientes()).isEqualTo(3);
    }

    @Test
    @DisplayName("debe excluir las facturas anuladas del monto facturado del mes")
    void debeExcluirFacturasAnuladasDelMonto() {
        when(ordenTrabajoRepository.findByFiltros(any(), anyList())).thenReturn(List.of());
        when(presupuestoRepository.findAll()).thenReturn(List.of());
        when(facturaRepository.findByFiltros(any(), any(), any(), any(), any(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(
                        facturaEmitida(BigDecimal.valueOf(1000)),
                        Factura.builder().estado(EstadoFactura.ANULADA).montoFacturado(BigDecimal.valueOf(300)).build()
                ));

        DashboardResumen resumen = useCase.obtener();

        assertThat(resumen.montoFacturadoUltimoMes()).isEqualTo(BigDecimal.valueOf(1000));
    }
}
