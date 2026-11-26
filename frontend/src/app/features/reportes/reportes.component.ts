import { Component, computed, DestroyRef, inject, signal } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Subscription } from 'rxjs';
import { ChartModule } from 'primeng/chart';
import { DatePicker } from 'primeng/datepicker';
import { ReporteService } from './services/reporte.service';
import { AgrupacionReporte, FormaPago, ReporteResponse } from './models/reporte.model';

const MESES = ['ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dic'];

const ETIQUETAS_FORMA_PAGO: Record<FormaPago, string> = {
  EFECTIVO: 'Efectivo',
  TRANSFERENCIA_BANCARIA: 'Transferencia',
};

// Chart.js no lee las variables CSS, por eso se repiten los colores de styles.scss
// (--primary-color, --info-color, --text-main y --border-color).
const COLOR_PRIMARIO = '#1e3a8a';
const COLOR_INFO = '#3b82f6';
const COLORES_FORMA_PAGO: Record<FormaPago, string> = {
  EFECTIVO: COLOR_PRIMARIO,
  TRANSFERENCIA_BANCARIA: COLOR_INFO,
};
const COLOR_TEXTO = '#334155';
const COLOR_GRILLA = '#e2e8f0';

const MONTO_OCULTO = '$ ••••••';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CurrencyPipe, DecimalPipe, FormsModule, ChartModule, DatePicker],
  templateUrl: './reportes.component.html',
  styleUrl: './reportes.component.scss',
})
export class ReportesComponent {
  readonly reporteService = inject(ReporteService);
  private readonly destroyRef = inject(DestroyRef);
  private peticion?: Subscription;

  readonly desde = signal<Date | null>(this.primerDiaHaceMeses(5));
  readonly hasta = signal<Date | null>(new Date());
  readonly hoy = new Date();
  readonly montoOculto = MONTO_OCULTO;
  readonly montoVisible = signal(false);

  readonly rangoInvalido = computed(() => {
    const desde = this.desde();
    const hasta = this.hasta();
    return !desde || !hasta || desde > hasta;
  });

  readonly reporte = this.reporteService.reporte;

  readonly hayFacturacion = computed(() => (this.reporte()?.facturacion ?? []).some((p) => p.monto > 0));
  readonly hayServicios = computed(() => (this.reporte()?.topServicios.length ?? 0) > 0);

  readonly formasPago = computed(() => {
    const formas = this.reporte()?.formasPago ?? [];
    const total = formas.reduce((suma, f) => suma + f.monto, 0);
    return formas.map((f) => ({
      etiqueta: ETIQUETAS_FORMA_PAGO[f.formaPago],
      color: COLORES_FORMA_PAGO[f.formaPago],
      monto: f.monto,
      porcentaje: total > 0 ? Math.round((f.monto / total) * 100) : 0,
    }));
  });

  readonly graficoFacturacion = computed(() => {
    const r = this.reporte();
    return {
      labels: (r?.facturacion ?? []).map((p) => this.etiquetaPeriodo(p.fecha, r!.agrupacion)),
      datasets: [{
        label: 'Facturado',
        data: (r?.facturacion ?? []).map((p) => p.monto),
        backgroundColor: COLOR_PRIMARIO,
      }],
    };
  });

  readonly graficoServicios = computed(() => ({
    labels: (this.reporte()?.topServicios ?? []).map((s) => s.nombre),
    datasets: [{
      label: 'Veces',
      data: (this.reporte()?.topServicios ?? []).map((s) => s.cantidadOrdenes),
      backgroundColor: COLOR_PRIMARIO,
    }],
  }));

  readonly opcionesMonto = computed(() => {
    const visible = this.montoVisible();
    return this.opciones({
      scales: {
        x: { grid: { display: false }, ticks: { color: COLOR_TEXTO } },
        y: { beginAtZero: true, grid: { color: COLOR_GRILLA }, ticks: { display: visible, color: COLOR_TEXTO, callback: (v: unknown) => this.formatearMonto(Number(v)) } },
      },
      plugins: {
        legend: { display: false },
        tooltip: { enabled: visible, callbacks: { label: (ctx: any) => this.formatearMonto(ctx.parsed.y) } },
      },
    });
  });

  readonly opcionesServicios = this.opciones({
    indexAxis: 'y',
    scales: {
      x: { beginAtZero: true, grid: { color: COLOR_GRILLA }, ticks: { color: COLOR_TEXTO, precision: 0 } },
      y: { grid: { display: false }, ticks: { color: COLOR_TEXTO } },
    },
    plugins: { legend: { display: false } },
  });

  constructor() {
    this.destroyRef.onDestroy(() => this.peticion?.unsubscribe());
    this.cargar();
  }

  cambiarDesde(fecha: Date | null) {
    this.desde.set(fecha);
    this.cargar();
  }

  cambiarHasta(fecha: Date | null) {
    this.hasta.set(fecha);
    this.cargar();
  }

  toggleMonto() {
    this.montoVisible.update((v) => !v);
  }

  periodoTexto(agrupacion: AgrupacionReporte): string {
    return agrupacion === 'MES' ? 'por mes' : 'por día';
  }

  tiempoPromedio(r: ReporteResponse): string {
    return r.tiempoPromedioReparacionDias == null
      ? '—'
      : `${r.tiempoPromedioReparacionDias.toLocaleString('es-AR')} días`;
  }

  private cargar() {
    if (this.rangoInvalido()) return;

    this.peticion?.unsubscribe();
    this.peticion = this.reporteService
      .obtener(this.formatearFecha(this.desde()!), this.formatearFecha(this.hasta()!))
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({ error: () => {} });
  }

  private opciones(extra: Record<string, unknown>) {
    return { responsive: true, maintainAspectRatio: false, ...extra };
  }

  private formatearMonto(valor: number): string {
    return valor.toLocaleString('es-AR', { style: 'currency', currency: 'ARS', maximumFractionDigits: 0 });
  }

  // Se arma a mano desde el string para evitar el corrimiento por zona horaria de new Date('yyyy-MM-dd').
  private etiquetaPeriodo(fecha: string, agrupacion: 'DIA' | 'MES'): string {
    const [anio, mes, dia] = fecha.split('-').map(Number);
    return agrupacion === 'MES'
      ? `${MESES[mes - 1]} ${String(anio).slice(2)}`
      : `${String(dia).padStart(2, '0')}/${String(mes).padStart(2, '0')}`;
  }

  private primerDiaHaceMeses(meses: number): Date {
    const hoy = new Date();
    return new Date(hoy.getFullYear(), hoy.getMonth() - meses, 1);
  }

  private formatearFecha(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
  }
}
