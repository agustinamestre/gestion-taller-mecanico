package com.taller.gestion_taller.application.usecases.reporte;

import com.taller.gestion_taller.application.dto.Reporte;
import com.taller.gestion_taller.application.dto.Reporte.Agrupacion;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.repositories.ReporteRepository;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.CantidadPorFecha;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.MontoPorFecha;
import com.taller.gestion_taller.domain.repositories.ReporteRepository.ServicioEnOrden;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class ObtenerReporteUseCase implements ObtenerReporte {

    private static final int MESES_POR_DEFECTO = 6;
    private static final long MAX_DIAS_AGRUPADO_POR_DIA = 62;
    private static final int TOP_SERVICIOS = 5;

    private final ReporteRepository reporteRepository;

    @Override
    public Reporte obtener(LocalDate desde, LocalDate hasta) {
        LocalDate hastaFinal = hasta != null ? hasta : LocalDate.now();
        LocalDate desdeFinal = desde != null
                ? desde
                : hastaFinal.withDayOfMonth(1).minusMonths(MESES_POR_DEFECTO - 1);

        if (desdeFinal.isAfter(hastaFinal)) {
            throw new BusinessRunTimeException(
                    BusinessErrors.campoInvalido("desde", "no puede ser posterior a la fecha hasta"));
        }

        Agrupacion agrupacion = ChronoUnit.DAYS.between(desdeFinal, hastaFinal) <= MAX_DIAS_AGRUPADO_POR_DIA
                ? Agrupacion.DIA
                : Agrupacion.MES;

        List<MontoPorFecha> facturacionDiaria = reporteRepository.facturacionPorDia(desdeFinal, hastaFinal);
        List<CantidadPorFecha> ingresadas = reporteRepository.ordenesIngresadasPorDia(desdeFinal, hastaFinal);
        List<CantidadPorFecha> entregadas = reporteRepository.ordenesEntregadasPorDia(desdeFinal, hastaFinal);

        return new Reporte(
                desdeFinal,
                hastaFinal,
                agrupacion,
                facturacionDiaria.stream().map(MontoPorFecha::monto).reduce(BigDecimal.ZERO, BigDecimal::add),
                ingresadas.stream().mapToLong(CantidadPorFecha::cantidad).sum(),
                entregadas.stream().mapToLong(CantidadPorFecha::cantidad).sum(),
                calcularTiempoPromedioReparacion(desdeFinal, hastaFinal),
                armarSerieFacturacion(facturacionDiaria, desdeFinal, hastaFinal, agrupacion),
                armarSerieOrdenes(ingresadas, entregadas, desdeFinal, hastaFinal, agrupacion),
                calcularTopServicios(desdeFinal, hastaFinal),
                reporteRepository.facturacionPorFormaPago(desdeFinal, hastaFinal).stream()
                        .map(f -> new Reporte.FacturacionFormaPago(f.formaPago(), f.monto(), f.cantidad()))
                        .toList());
    }

    private Double calcularTiempoPromedioReparacion(LocalDate desde, LocalDate hasta) {
        return reporteRepository.ordenesFinalizadas(desde, hasta).stream()
                .mapToLong(o -> ChronoUnit.DAYS.between(o.fechaIngreso(), o.fechaFinalizacion()))
                .average()
                .stream()
                .mapToObj(promedio -> Math.round(promedio * 10) / 10.0)
                .findFirst()
                .orElse(null);
    }

    // Una orden cuenta una sola vez por servicio aunque esté en el presupuesto y también en los ítems de la orden.
    private List<Reporte.ServicioTop> calcularTopServicios(LocalDate desde, LocalDate hasta) {
        Map<Long, String> nombres = new HashMap<>();
        Map<Long, Set<Long>> ordenesPorServicio = new HashMap<>();

        for (ServicioEnOrden s : reporteRepository.serviciosPorOrden(desde, hasta)) {
            nombres.putIfAbsent(s.productoId(), s.nombre());
            ordenesPorServicio.computeIfAbsent(s.productoId(), id -> new HashSet<>()).add(s.ordenId());
        }

        return ordenesPorServicio.entrySet().stream()
                .map(e -> new Reporte.ServicioTop(e.getKey(), nombres.get(e.getKey()), e.getValue().size()))
                .sorted(Comparator.comparingLong(Reporte.ServicioTop::cantidadOrdenes).reversed()
                        .thenComparing(Reporte.ServicioTop::nombre))
                .limit(TOP_SERVICIOS)
                .toList();
    }

    private List<Reporte.PuntoFacturacion> armarSerieFacturacion(List<MontoPorFecha> diaria, LocalDate desde,
                                                                  LocalDate hasta, Agrupacion agrupacion) {
        Map<LocalDate, BigDecimal> montos = new HashMap<>();
        diaria.forEach(m -> montos.merge(inicioDelPeriodo(m.fecha(), agrupacion), m.monto(), BigDecimal::add));

        return periodos(desde, hasta, agrupacion).stream()
                .map(p -> new Reporte.PuntoFacturacion(p, montos.getOrDefault(p, BigDecimal.ZERO)))
                .toList();
    }

    private List<Reporte.PuntoOrdenes> armarSerieOrdenes(List<CantidadPorFecha> ingresadas,
                                                         List<CantidadPorFecha> entregadas, LocalDate desde,
                                                         LocalDate hasta, Agrupacion agrupacion) {
        Map<LocalDate, Long> ingresadasPorPeriodo = agrupar(ingresadas, agrupacion);
        Map<LocalDate, Long> entregadasPorPeriodo = agrupar(entregadas, agrupacion);

        return periodos(desde, hasta, agrupacion).stream()
                .map(p -> new Reporte.PuntoOrdenes(p,
                        ingresadasPorPeriodo.getOrDefault(p, 0L),
                        entregadasPorPeriodo.getOrDefault(p, 0L)))
                .toList();
    }

    private Map<LocalDate, Long> agrupar(List<CantidadPorFecha> cantidades, Agrupacion agrupacion) {
        Map<LocalDate, Long> resultado = new HashMap<>();
        cantidades.forEach(c -> resultado.merge(inicioDelPeriodo(c.fecha(), agrupacion), c.cantidad(), Long::sum));
        return resultado;
    }

    private LocalDate inicioDelPeriodo(LocalDate fecha, Agrupacion agrupacion) {
        return agrupacion == Agrupacion.MES ? fecha.withDayOfMonth(1) : fecha;
    }

    // Incluye los períodos sin datos para que el gráfico no se corte.
    private List<LocalDate> periodos(LocalDate desde, LocalDate hasta, Agrupacion agrupacion) {
        List<LocalDate> periodos = new ArrayList<>();
        if (agrupacion == Agrupacion.DIA) {
            for (LocalDate d = desde; !d.isAfter(hasta); d = d.plusDays(1)) {
                periodos.add(d);
            }
        } else {
            for (YearMonth m = YearMonth.from(desde); !m.isAfter(YearMonth.from(hasta)); m = m.plusMonths(1)) {
                periodos.add(m.atDay(1));
            }
        }
        return periodos;
    }
}
