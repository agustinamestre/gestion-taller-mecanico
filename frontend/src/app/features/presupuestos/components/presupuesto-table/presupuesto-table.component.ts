import { Component, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SelectModule } from 'primeng/select';
import { PresupuestoService } from '../../services/presupuesto.service';
import { DatePicker } from 'primeng/datepicker'
import { EstadoPresupuesto, PresupuestoSummaryResponse, TRANSICIONES_VALIDAS } from '../../models/presupuesto.model';

@Component({
  selector: 'app-presupuesto-busqueda',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, InputTextModule, ButtonModule, TableModule, SelectModule, DatePicker],
  templateUrl: './presupuesto-table.component.html',
  styleUrl: './presupuesto-table.component.scss',
})
export class PresupuestoTableComponent {
  readonly presupuestoService = inject(PresupuestoService);
  private readonly location = inject(Location);

  readonly nuevoPresupuesto = output<string | null>();
  readonly verDetalle = output<number>();

  readonly patente = signal('');
  readonly fechaDesde = signal<Date | null>(null);
  readonly fechaHasta = signal<Date | null>(null);
  readonly cambiandoEstadoId = signal<number | null>(null);

  constructor() {
    this.presupuestoService.listar().subscribe();
  }

  volver() {
    this.location.back();
  }

  buscar() {
    const patente = this.patente().trim().toUpperCase();
    const desde = this.fechaDesde();
    const hasta = this.fechaHasta();

    this.presupuestoService.listar({
      patente: patente || undefined,
      fechaDesde: desde ? this.formatearFecha(desde) : undefined,
      fechaHasta: hasta ? this.formatearFecha(hasta) : undefined,
    }).subscribe();
  }

  limpiarFiltros() {
    this.patente.set('');
    this.fechaDesde.set(null);
    this.fechaHasta.set(null);
    this.presupuestoService.listar().subscribe();
  }

  get tieneFiltrosActivos(): boolean {
    return !!this.patente().trim() || this.fechaDesde() != null || this.fechaHasta() != null;
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

  private formatearFecha(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
  }
}