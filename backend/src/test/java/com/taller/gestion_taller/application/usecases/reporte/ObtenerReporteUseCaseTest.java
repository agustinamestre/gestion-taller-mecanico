package com.taller.gestion_taller.application.usecases.reporte;

import com.taller.gestion_taller.application.dto.Reporte;
import com.taller.gestion_taller.application.dto.Reporte.Agrupacion;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.repositories.ReporteRepository;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.CantidadPorFecha;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.FechasOrden;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.MontoPorFecha;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.MontoPorFormaPago;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.ServicioEnOrden;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerReporteUseCaseTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ObtenerReporteUseCase useCase;

    private void sinDatos() {
        lenient().when(reporteRepository.facturacionPorDia(any(), any())).thenReturn(List.of());
        lenient().when(reporteRepository.facturacionPorFormaPago(any(), any())).thenReturn(List.of());
        lenient().when(reporteRepository.ordenesIngresadasPorDia(any(), any())).thenReturn(List.of());
        lenient().when(reporteRepository.ordenesEntregadasPorDia(any(), any())).thenReturn(List.of());
        lenient().when(reporteRepository.ordenesFinalizadas(any(), any())).thenReturn(List.of());
        lenient().when(reporteRepository.serviciosPorOrden(any(), any())).thenReturn(List.of());
    }

    @Test
    @DisplayName("debe lanzar excepcion si desde es posterior a hasta")
    void debeRechazarRangoInvalido() {
        assertThatThrownBy(() -> useCase.obtener(LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 1)))
                .isInstanceOf(BusinessRunTimeException.class);
    }

    @Test
    @DisplayName("sin fechas debe usar los ultimos 6 meses agrupados por mes")
    void debeUsarUltimosSeisMesesPorDefecto() {
        sinDatos();

        Reporte reporte = useCase.obtener(null, null);

        assertThat(reporte.agrupacion()).isEqualTo(Agrupacion.MES);
        assertThat(reporte.facturacion()).hasSize(6);
        assertThat(reporte.hasta()).isEqualTo(LocalDate.now());
        assertThat(reporte.desde()).isEqualTo(LocalDate.now().withDayOfMonth(1).minusMonths(5));
    }

    @Test
    @DisplayName("rango corto se agrupa por dia y rellena los dias sin datos")
    void debeAgruparPorDiaEnRangosCortos() {
        sinDatos();
        LocalDate desde = LocalDate.of(2026, 11, 1);
        LocalDate hasta = LocalDate.of(2026, 11, 10);
        when(reporteRepository.facturacionPorDia(desde, hasta))
                .thenReturn(List.of(new MontoPorFecha(LocalDate.of(2026, 11, 3), new BigDecimal("1500.00"))));

        Reporte reporte = useCase.obtener(desde, hasta);

        assertThat(reporte.agrupacion()).isEqualTo(Agrupacion.DIA);
        assertThat(reporte.facturacion()).hasSize(10);
        assertThat(reporte.facturacion().get(2).monto()).isEqualByComparingTo("1500.00");
        assertThat(reporte.facturacion().get(0).monto()).isEqualByComparingTo("0");
        assertThat(reporte.totalFacturado()).isEqualByComparingTo("1500.00");
    }

    @Test
    @DisplayName("rango largo suma facturacion y ordenes por mes")
    void debeAgruparPorMes() {
        sinDatos();
        LocalDate desde = LocalDate.of(2026, 8, 15);
        LocalDate hasta = LocalDate.of(2026, 11, 17);
        when(reporteRepository.facturacionPorDia(desde, hasta)).thenReturn(List.of(
                new MontoPorFecha(LocalDate.of(2026, 9, 2), new BigDecimal("100")),
                new MontoPorFecha(LocalDate.of(2026, 9, 20), new BigDecimal("50"))));
        when(reporteRepository.ordenesIngresadasPorDia(desde, hasta)).thenReturn(List.of(
                new CantidadPorFecha(LocalDate.of(2026, 9, 2), 2),
                new CantidadPorFecha(LocalDate.of(2026, 9, 21), 3)));
        when(reporteRepository.ordenesEntregadasPorDia(desde, hasta)).thenReturn(List.of(
                new CantidadPorFecha(LocalDate.of(2026, 10, 5), 4)));

        Reporte reporte = useCase.obtener(desde, hasta);

        assertThat(reporte.agrupacion()).isEqualTo(Agrupacion.MES);
        assertThat(reporte.facturacion()).extracting(Reporte.PuntoFacturacion::fecha)
                .containsExactly(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 1));
        assertThat(reporte.facturacion().get(1).monto()).isEqualByComparingTo("150");
        assertThat(reporte.ordenes().get(1).ingresadas()).isEqualTo(5);
        assertThat(reporte.ordenes().get(2).entregadas()).isEqualTo(4);
        assertThat(reporte.ordenesIngresadas()).isEqualTo(5);
        assertThat(reporte.ordenesEntregadas()).isEqualTo(4);
    }

    @Test
    @DisplayName("tiempo promedio de reparacion es null sin ordenes finalizadas y se redondea a un decimal")
    void debeCalcularTiempoPromedio() {
        sinDatos();
        LocalDate desde = LocalDate.of(2026, 11, 1);
        LocalDate hasta = LocalDate.of(2026, 11, 30);

        assertThat(useCase.obtener(desde, hasta).tiempoPromedioReparacionDias()).isNull();

        when(reporteRepository.ordenesFinalizadas(desde, hasta)).thenReturn(List.of(
                new FechasOrden(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3)),
                new FechasOrden(LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 5)),
                new FechasOrden(LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 13))));

        assertThat(useCase.obtener(desde, hasta).tiempoPromedioReparacionDias()).isEqualTo(1.7);
    }

    @Test
    @DisplayName("top servicios cuenta ordenes distintas, ordena y limita a 5")
    void debeArmarTopServicios() {
        sinDatos();
        LocalDate desde = LocalDate.of(2026, 11, 1);
        LocalDate hasta = LocalDate.of(2026, 11, 30);
        when(reporteRepository.serviciosPorOrden(desde, hasta)).thenReturn(List.of(
                new ServicioEnOrden(1L, "Cambio de aceite", 10L),
                new ServicioEnOrden(1L, "Cambio de aceite", 10L),
                new ServicioEnOrden(1L, "Cambio de aceite", 11L),
                new ServicioEnOrden(2L, "Alineación", 10L),
                new ServicioEnOrden(3L, "Balanceo", 12L),
                new ServicioEnOrden(4L, "Diagnóstico", 12L),
                new ServicioEnOrden(5L, "Cambio de frenos", 12L),
                new ServicioEnOrden(6L, "Cambio de correa", 12L)));

        List<Reporte.ServicioTop> top = useCase.obtener(desde, hasta).topServicios();

        assertThat(top).hasSize(5);
        assertThat(top.get(0).nombre()).isEqualTo("Cambio de aceite");
        assertThat(top.get(0).cantidadOrdenes()).isEqualTo(2);
        assertThat(top.get(1).cantidadOrdenes()).isEqualTo(1);
    }

    @Test
    @DisplayName("formas de pago se devuelven tal cual las informa el repositorio")
    void debeMapearFormasDePago() {
        sinDatos();
        LocalDate desde = LocalDate.of(2026, 11, 1);
        LocalDate hasta = LocalDate.of(2026, 11, 30);
        when(reporteRepository.facturacionPorFormaPago(desde, hasta)).thenReturn(List.of(
                new MontoPorFormaPago(FormaPago.EFECTIVO, new BigDecimal("300"), 2)));

        List<Reporte.FacturacionFormaPago> formas = useCase.obtener(desde, hasta).formasPago();

        assertThat(formas).hasSize(1);
        assertThat(formas.get(0).formaPago()).isEqualTo(FormaPago.EFECTIVO);
        assertThat(formas.get(0).cantidad()).isEqualTo(2);
    }
}
