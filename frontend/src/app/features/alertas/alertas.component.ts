import { Component, inject, signal } from '@angular/core';
import { AlertaService } from './services/alerta.service';
import { VehiculoService } from '../vehiculos/services/vehiculo.service';
import { Alerta } from './models/alerta.model';
import { AlertaTableComponent } from './components/alerta-table/alerta-table.component';
import { AlertaContactarFormComponent } from './components/alerta-contactar-form/alerta-contactar-form.component';
import { AlertaDetailComponent } from './components/alerta-detail/alerta-detail.component';

export type VistaAlerta = 'tabla' | 'contactar' | 'detalle';

@Component({
  selector: 'app-alertas',
  standalone: true,
  imports: [AlertaTableComponent, AlertaContactarFormComponent, AlertaDetailComponent],
  templateUrl: './alertas.component.html',
  styleUrl: './alertas.component.scss',
})
export class AlertasComponent {
  private readonly alertaService = inject(AlertaService);
  private readonly vehiculoService = inject(VehiculoService);

  readonly vista = signal<VistaAlerta>('tabla');

  verDetalle(alerta: Alerta) {
    this.alertaService.alertaSeleccionada.set(alerta);
    this.vista.set('detalle');
  }

  abrirContactar(alerta: Alerta) {
    this.alertaService.alertaSeleccionada.set(alerta);
    this.vista.set('contactar');
  }

  volver() {
    this.alertaService.limpiarSeleccion();
    this.vehiculoService.limpiarSeleccion();
    this.vista.set('tabla');
  }
}
