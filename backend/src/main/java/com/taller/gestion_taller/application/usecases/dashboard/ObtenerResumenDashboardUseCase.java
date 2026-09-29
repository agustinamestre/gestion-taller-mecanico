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
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ObtenerResumenDashboardUseCase implements ObtenerResumenDashboard {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final FacturaRepository facturaRepository;
    private final AlertaRepository alertaRepository;

    @Override
    public DashboardResumen obtener() {
        long ordenesEnCurso = ordenTrabajoRepository
                .findByFiltros(null, List.of(EstadoOrdenTrabajo.INGRESADO, EstadoOrdenTrabajo.EN_REPARACION))
                .size();

        long ordenesListasParaEntregar = ordenTrabajoRepository
                .findByFiltros(null, List.of(EstadoOrdenTrabajo.FINALIZADO))
                .size();

        List<Presupuesto> presupuestos = presupuestoRepository.findAll();

        long presupuestosPendientes = presupuestos.stream()
                .filter(presupuesto -> presupuesto.getEstado() == EstadoPresupuesto.PENDIENTE)
                .count();

        long presupuestosAprobadosSinOrden = presupuestos.stream()
                .filter(presupuesto -> presupuesto.getEstado() == EstadoPresupuesto.APROBADO)
                .count();

        LocalDate hoy = LocalDate.now();
        LocalDate primerDiaDelMes = hoy.withDayOfMonth(1);

        List<Factura> facturasDelMes = facturaRepository
                .findByFiltros(null, null, null, null, null, primerDiaDelMes, hoy);

        BigDecimal montoFacturadoUltimoMes = Factura.sumarMontoFacturado(
                facturasDelMes.stream()
                        .filter(factura -> factura.getEstado() == EstadoFactura.EMITIDA)
                        .toList());

        long alertasPendientes = alertaRepository.countByContactadoFalse();

        return new DashboardResumen(
                ordenesEnCurso,
                ordenesListasParaEntregar,
                presupuestosPendientes,
                presupuestosAprobadosSinOrden,
                montoFacturadoUltimoMes,
                alertasPendientes);
    }
}
