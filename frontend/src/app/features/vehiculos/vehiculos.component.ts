import { Component, effect, inject, signal } from '@angular/core';
import { VehiculoService } from './services/vehiculo.service';
import { NotificationService } from '../../shared/services/notification.service';
import { VehiculoDetailComponent } from './components/vehiculo-detail/vehiculo-detail.component';
import { VehiculoFormComponent } from './components/vehiculo-form/vehiculo-form.component';
import { VehiculoKmDialogComponent } from './components/vehiculo-km-dialog/vehiculo-km-dialog.component';
import { VehiculoResponse } from './models/vehiculo.model';
import { VehiculoSearchComponent } from './components/vehiculo-search/vehiculo-search.component';
import { Router } from '@angular/router';

export type VistaVehiculo = 'busqueda' | 'detalle' | 'form-nuevo' | 'form-editar';

@Component({
  selector: 'app-vehiculos',
  standalone: true,
  imports: [
    VehiculoSearchComponent,
    VehiculoDetailComponent,
    VehiculoFormComponent,
    VehiculoKmDialogComponent,
  ],
  templateUrl: './vehiculos.component.html',
  styleUrl: './vehiculos.component.scss',
})
export class VehiculosComponent {
  private readonly vehiculoService = inject(VehiculoService);
  private readonly notification = inject(NotificationService);
  private readonly router = inject(Router);

  readonly vista = signal<VistaVehiculo>('busqueda');
  readonly kmDialogVisible = signal(false);
  readonly clienteIdPreseteado = signal<number | null>(null);

  private modoActual: VistaVehiculo = 'busqueda';

  constructor() {
    const nav = this.router.getCurrentNavigation();
    const clienteId = nav?.extras?.state?.['clienteId'] ?? null;

    if (clienteId) {
      this.clienteIdPreseteado.set(clienteId);
      this.vista.set('form-nuevo');
    }

    effect(() => {
      if (this.vehiculoService.vehiculoActual() !== null && this.vista() === 'busqueda') {
        this.vista.set('detalle');
      }
    });
  }

  onVehiculoEncontrado() {
    this.vista.set('detalle');
  }

  abrirNuevo() {
    this.vehiculoService.limpiarSeleccion();
    this.modoActual = 'form-nuevo';
    this.vista.set('form-nuevo');
  }

  abrirEditar() {
    this.modoActual = 'form-editar';
    this.vista.set('form-editar');
  }

  abrirKmDialog() {
    this.kmDialogVisible.set(true);
  }

  volver() {
    this.vehiculoService.limpiarSeleccion();
    this.vista.set('busqueda');
  }

  volverAlDetalle() {
    this.vista.set('detalle');
  }

  onGuardado() {
    const mensaje =
      this.modoActual === 'form-nuevo'
        ? 'El vehículo fue registrado correctamente.'
        : 'El vehículo fue actualizado correctamente.';
    this.notification.exito(mensaje);
    this.vista.set('detalle');
  }

  onKmActualizado() {
    this.kmDialogVisible.set(false);
    this.notification.exito('Kilometraje actualizado correctamente.');
  }

  onDesactivado() {
    this.notification.advertencia('El vehículo fue desactivado del sistema.');
    this.vista.set('busqueda');
  }
}