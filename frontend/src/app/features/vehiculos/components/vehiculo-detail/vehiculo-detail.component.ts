import { Component, inject, output } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { VehiculoService } from '../../services/vehiculo.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { signal } from '@angular/core';

@Component({
  selector: 'app-vehiculo-detail',
  standalone: true,
  imports: [DecimalPipe, ButtonModule, TagModule, ConfirmDialogComponent],
  templateUrl: './vehiculo-detail.component.html',
  styleUrl: './vehiculo-detail.component.scss',
})
export class VehiculoDetailComponent {
  readonly vehiculoService = inject(VehiculoService);

  readonly editar = output<void>();
  readonly actualizarKm = output<void>();
  readonly volver = output<void>();
  readonly reactivado = output<void>();
  readonly desactivado = output<void>();

  readonly dialogVisible = signal(false);
  readonly accion = signal<'reactivar' | 'desactivar'>('reactivar');

  get vehiculo() {
    return this.vehiculoService.vehiculoActual();
  }

  abrirDialogo(accion: 'reactivar' | 'desactivar') {
    this.accion.set(accion);
    this.dialogVisible.set(true);
  }

  onConfirmado() {
    const vehiculo = this.vehiculo;
    if (!vehiculo) return;
    const operacion = this.accion() === 'reactivar'
      ? this.vehiculoService.reactivar(vehiculo.id)
      : this.vehiculoService.desactivar(vehiculo.id);

    operacion.subscribe({
      next: () => {
        this.dialogVisible.set(false);
        if (this.accion() === 'reactivar') {
          this.reactivado.emit();
        } else {
          this.desactivado.emit();
        }
      },
    });
  }
}