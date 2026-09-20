import { Component, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SelectModule } from 'primeng/select';
import { PresupuestoService } from '../../services/presupuesto.service';
import { EstadoPresupuesto, PresupuestoSummaryResponse, TRANSICIONES_VALIDAS } from '../../models/presupuesto.model';

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
    this.presupuestoService.listar().subscribe();
  }

  get tieneFiltrosActivos(): boolean {
    return !!this.patente().trim() || !!this.dni().trim();
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