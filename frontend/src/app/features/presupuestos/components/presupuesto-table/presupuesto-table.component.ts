import { Component, computed, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SelectModule } from 'primeng/select';
import { PresupuestoService } from '../../services/presupuesto.service';
import { EstadoPresupuesto, PresupuestoSummaryResponse, TRANSICIONES_VALIDAS } from '../../models/presupuesto.model';

type FiltroEstado = 'ACTIVOS' | 'TODOS' | EstadoPresupuesto;

const ESTADOS_ACTIVOS: EstadoPresupuesto[] = ['PENDIENTE', 'APROBADO'];

@Component({
  selector: 'app-presupuesto-busqueda',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, InputTextModule, ButtonModule, TableModule, SelectModule],
  templateUrl: './presupuesto-table.component.html',
  styleUrl: './presupuesto-table.component.scss',
})
export class PresupuestoTableComponent {
  readonly presupuestoService = inject(PresupuestoService);
  private readonly location = inject(Location);

  readonly nuevoPresupuesto = output<string | null>();
  readonly verDetalle = output<number>();

  readonly patente = signal('');
  readonly dni = signal('');
  readonly cambiandoEstadoId = signal<number | null>(null);
  readonly filtroEstado = signal<FiltroEstado>('ACTIVOS');

  readonly opcionesFiltroEstado: { label: string; value: FiltroEstado }[] = [
    { label: 'Activos', value: 'ACTIVOS' },
    { label: 'Todos', value: 'TODOS' },
    { label: 'Pendiente', value: 'PENDIENTE' },
    { label: 'Aprobado', value: 'APROBADO' },
    { label: 'Rechazado', value: 'RECHAZADO' },
    { label: 'Vencido', value: 'VENCIDO' },
    { label: 'Utilizado', value: 'UTILIZADO' },
    { label: 'Cancelado', value: 'CANCELADO' },
  ];

  readonly listadoFiltrado = computed(() => {
    const filtro = this.filtroEstado();
    const listado = this.presupuestoService.listado();

    if (filtro === 'TODOS') return listado;
    if (filtro === 'ACTIVOS') return listado.filter(p => ESTADOS_ACTIVOS.includes(p.estado));
    return listado.filter(p => p.estado === filtro);
  });

  constructor() {
    this.presupuestoService.listar().subscribe();
  }

  volver() {
    this.location.back();
  }

  buscar() {
    const patente = this.patente().trim().toUpperCase();
    const dni = this.dni().trim();

    this.presupuestoService.listar({
      patente: patente || undefined,
      dni: dni || undefined,
    }).subscribe();
  }

  limpiarFiltros() {
    this.patente.set('');
    this.dni.set('');
    this.filtroEstado.set('ACTIVOS');
    this.presupuestoService.listar().subscribe();
  }

  get tieneFiltrosActivos(): boolean {
    return !!this.patente().trim() || !!this.dni().trim() || this.filtroEstado() !== 'ACTIVOS';
  }

  transicionesDe(presupuesto: PresupuestoSummaryResponse): EstadoPresupuesto[] {
    return TRANSICIONES_VALIDAS[presupuesto.estado];
  }

  onCambiarEstado(presupuesto: PresupuestoSummaryResponse, nuevoEstado: EstadoPresupuesto) {
    if (nuevoEstado === presupuesto.estado) return;

    this.cambiandoEstadoId.set(presupuesto.id);
    this.presupuestoService.cambiarEstado(presupuesto.id, { nuevoEstado }).subscribe({
      next: () => this.cambiandoEstadoId.set(null),
      error: () => this.cambiandoEstadoId.set(null),
    });
  }

}