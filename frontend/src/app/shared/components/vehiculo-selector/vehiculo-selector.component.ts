import { Component, effect, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutoCompleteModule, AutoCompleteCompleteEvent, AutoCompleteSelectEvent } from 'primeng/autocomplete';
import { VehiculoResponse } from '../../../features/vehiculos/models/vehiculo.model';
import { VehiculoService } from '../../../features/vehiculos/services/vehiculo.service';

interface VehiculoAutoComplete extends VehiculoResponse {
  etiquetaCompleta: string;
}

@Component({
  selector: 'app-vehiculo-selector',
  standalone: true,
  imports: [FormsModule, AutoCompleteModule],
  templateUrl: './vehiculo-selector.component.html',
  styleUrl: './vehiculo-selector.component.scss',
})
export class VehiculoSelectorComponent implements OnInit {
  private readonly vehiculoService = inject(VehiculoService);

  readonly vehiculoIdInicial = input<number | null>(null);
  readonly placeholder = input('Buscar por patente...');
  readonly disabled = input(false);

  readonly vehiculoSeleccionado = output<VehiculoResponse | null>();

  seleccion: VehiculoAutoComplete | null = null;
  readonly sugerencias = signal<VehiculoAutoComplete[]>([]);

  ngOnInit() {}

  buscar(event: AutoCompleteCompleteEvent) {
    const texto = event.query.trim();
    this.vehiculoService.buscar(texto).subscribe({
      next: (vehiculos) => {
        this.sugerencias.set(vehiculos.map(v => this.toAutoComplete(v)));
      },
    });
  }

  onModelChange(value: VehiculoAutoComplete | string | null) {
    if (value === null || typeof value === 'string') return;
    this.seleccion = value;
  }

  onSeleccion(event: AutoCompleteSelectEvent) {
    const vehiculo = event.value as VehiculoAutoComplete;
    this.seleccion = vehiculo;
    setTimeout(() => {
      this.vehiculoSeleccionado.emit(vehiculo);
    }, 0);
  }

  onClear() {
    this.seleccion = null;
    this.vehiculoSeleccionado.emit(null);
  }

  private toAutoComplete(vehiculo: VehiculoResponse): VehiculoAutoComplete {
    return {
      ...vehiculo,
      etiquetaCompleta: `${vehiculo.patente} — ${vehiculo.marca} ${vehiculo.modelo}`,
    };
  }
}