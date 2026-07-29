import { Component, inject, input, output } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { VehiculoService } from '../../services/vehiculo.service';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-vehiculo-km-dialog',
  standalone: true,
  imports: [ReactiveFormsModule, DialogModule, InputNumberModule, ButtonModule, DecimalPipe],
  templateUrl: './vehiculo-km-dialog.component.html',
  styleUrl: './vehiculo-km-dialog.component.scss',
})
export class VehiculoKmDialogComponent {
  private readonly fb = inject(FormBuilder);
  readonly vehiculoService = inject(VehiculoService);

  readonly visible = input(false);
  readonly guardado = output<void>();
  readonly cancelado = output<void>();

  readonly form = this.fb.group({
    kilometrajeActual: [null as number | null, [Validators.required, Validators.min(0)]],
  });

  get kmActual() {
    return this.vehiculoService.vehiculoActual()?.kilometrajeActual ?? 0;
  }

  guardar() {
    if (this.form.invalid) return;
    const vehiculo = this.vehiculoService.vehiculoActual();
    if (!vehiculo) return;

    const km = this.form.getRawValue().kilometrajeActual!;
    this.vehiculoService.actualizarKilometraje(vehiculo.id, { kilometrajeActual: km }).subscribe({
      next: () => {
        this.form.reset();
        this.guardado.emit();
      },
    });
  }

  onCancelar() {
    this.form.reset();
    this.cancelado.emit();
  }
}