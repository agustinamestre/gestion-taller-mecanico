import { Component, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SelectModule } from 'primeng/select';
import { OrdenTrabajoService } from '../../services/orden-trabajo.service';
import {
  EstadoOrdenTrabajo,
  ESTADO_ORDEN_LABELS,
  OrdenTrabajoResponse,
  TRANSICIONES_VALIDAS_ORDEN,
} from '../../models/orden-trabajo.model';

@Component({
  selector: 'app-orden-trabajo-table',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, InputTextModule, ButtonModule, TableModule, SelectModule],
  templateUrl: './orden-trabajo-table.component.html',
  styleUrl: './orden-trabajo-table.component.scss',
})
export class OrdenTrabajoTableComponent {
  readonly ordenTrabajoService = inject(OrdenTrabajoService);
  private readonly location = inject(Location);
  private readonly route = inject(ActivatedRoute);

  readonly nuevaOrden = output<void>();
  readonly verDetalle = output<number>();

  readonly patente = signal('');
  readonly estadoFiltro = signal<EstadoOrdenTrabajo | null>(null);
  readonly cambiandoEstadoId = signal<number | null>(null);

  readonly opcionesEstado = [
    { label: 'Ingresado', value: 'INGRESADO' as EstadoOrdenTrabajo },
    { label: 'En reparación', value: 'EN_REPARACION' as EstadoOrdenTrabajo },
    { label: 'Finalizado', value: 'FINALIZADO' as EstadoOrdenTrabajo },
    { label: 'Entregado', value: 'ENTREGADO' as EstadoOrdenTrabajo },
    { label: 'Cancelado', value: 'CANCELADO' as EstadoOrdenTrabajo },
  ];

  constructor() {
    const estadosParam = this.route.snapshot.queryParamMap.getAll('estado') as EstadoOrdenTrabajo[];

    if (estadosParam.length === 1) {
      this.estadoFiltro.set(estadosParam[0]);
      this.ordenTrabajoService.listar({ estado: estadosParam[0] }).subscribe();
    } else if (estadosParam.length > 1) {
      this.ordenTrabajoService.listar({ estado: estadosParam }).subscribe();
    } else {
      this.ordenTrabajoService.listar().subscribe();
    }
  }

  volver() {
    this.location.back();
  }

  buscar() {
    const patente = this.patente().trim().toUpperCase();
    this.ordenTrabajoService.listar({
      patente: patente || undefined,
      estado: this.estadoFiltro() ?? undefined,
    }).subscribe();
  }

  limpiarFiltros() {
    this.patente.set('');
    this.estadoFiltro.set(null);
    this.ordenTrabajoService.listar().subscribe();
  }

  get tieneFiltrosActivos(): boolean {
    return !!this.patente().trim() || this.estadoFiltro() != null;
  }

  transicionesDe(orden: OrdenTrabajoResponse): EstadoOrdenTrabajo[] {
    const sinSaldoPendiente = orden.facturada || orden.total === 0;
    return TRANSICIONES_VALIDAS_ORDEN[orden.estado]
      .filter(estado => estado !== 'ENTREGADO' || sinSaldoPendiente);
  }

  formatearEstado(estado: EstadoOrdenTrabajo): string {
    return ESTADO_ORDEN_LABELS[estado];
  }

  onCambiarEstado(orden: OrdenTrabajoResponse, nuevoEstado: EstadoOrdenTrabajo) {
    if (nuevoEstado === orden.estado) return;

    this.cambiandoEstadoId.set(orden.id);
    this.ordenTrabajoService.cambiarEstado(orden.id, { nuevoEstado }).subscribe({
      next: () => this.cambiandoEstadoId.set(null),
      error: () => this.cambiandoEstadoId.set(null),
    });
  }
}
