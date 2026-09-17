import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutoCompleteModule, AutoCompleteCompleteEvent, AutoCompleteSelectEvent } from 'primeng/autocomplete';
import { PresupuestoSummaryResponse } from '../../../features/presupuestos/models/presupuesto.model';
import { PresupuestoService } from '../../../features/presupuestos/services/presupuesto.service';
import { CurrencyPipe } from '@angular/common';

interface PresupuestoAutoComplete extends PresupuestoSummaryResponse {
  etiquetaCompleta: string;
}

@Component({
  selector: 'app-presupuesto-selector',
  standalone: true,
  imports: [FormsModule, AutoCompleteModule, CurrencyPipe],
  templateUrl: './presupuesto-selector.component.html',
  styleUrl: './presupuesto-selector.component.scss',
})
export class PresupuestoSelectorComponent implements OnInit {
  private readonly presupuestoService = inject(PresupuestoService);

  readonly placeholder = input('Ingrese patente del vehiculo');
  readonly disabled = input(false);

  readonly presupuestoSeleccionado = output<PresupuestoSummaryResponse | null>();

  seleccion: PresupuestoAutoComplete | null = null;
  readonly sugerencias = signal<PresupuestoAutoComplete[]>([]);

  ngOnInit() {
    this.presupuestoService.listar().subscribe();
  }

  buscar(event: AutoCompleteCompleteEvent) {
    const texto = event.query.toUpperCase().trim();
    const base = this.presupuestoService.listado()
      .filter(p => p.estado === 'APROBADO')
      .map(p => this.toAutoComplete(p));

    this.sugerencias.set(
      texto
        ? base.filter(p => p.patenteVehiculo?.toUpperCase().includes(texto))
        : base
    );
  }

  onModelChange(value: PresupuestoAutoComplete | string | null) {
    if (value === null || typeof value === 'string') return;
    this.seleccion = value;
  }

  onSeleccion(event: AutoCompleteSelectEvent) {
    const presupuesto = event.value as PresupuestoAutoComplete;
    this.seleccion = presupuesto;
    setTimeout(() => {
      this.presupuestoSeleccionado.emit(presupuesto);
    }, 0);
  }

  onClear() {
    this.seleccion = null;
    this.presupuestoSeleccionado.emit(null);
  }

  private toAutoComplete(p: PresupuestoSummaryResponse): PresupuestoAutoComplete {
    return {
      ...p,
      etiquetaCompleta: `Patente: ${p.patenteVehiculo ?? 'sin vehículo'}`,
    };
  }
}