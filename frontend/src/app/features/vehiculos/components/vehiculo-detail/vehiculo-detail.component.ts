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
  readonly desactivado = output<void>();

  readonly dialogVisible = signal(false);

  get vehiculo() {
    return this.vehiculoService.vehiculoActual();
  }

  onConfirmado() {
    const vehiculo = this.vehiculo;
    if (!vehiculo) return;
    this.vehiculoService.desactivar(vehiculo.id).subscribe({
      next: () => {
        this.dialogVisible.set(false);
        this.desactivado.emit();
      },
    });
  }
}