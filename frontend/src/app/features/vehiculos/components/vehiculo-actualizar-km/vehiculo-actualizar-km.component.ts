import { Component, computed, inject, output } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { VehiculoService } from '../../services/vehiculo.service';

@Component({
  selector: 'app-vehiculo-actualizar-km',
  standalone: true,
  imports: [ReactiveFormsModule, DecimalPipe, InputNumberModule, ButtonModule],
  templateUrl: './vehiculo-actualizar-km.component.html',
  styleUrl: './vehiculo-actualizar-km.component.scss',
})
export class VehiculoActualizarKmComponent {
  private readonly fb = inject(FormBuilder);
  readonly vehiculoService = inject(VehiculoService);

  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly vehiculo = computed(() => this.vehiculoService.vehiculoActual());
  readonly kmActual = computed(() => this.vehiculo()?.kilometrajeActual ?? 0);

  readonly form = this.fb.group({
    kilometrajeActual: [null as number | null, [Validators.required, Validators.min(0)]],
  });

  campo(nombre: string): AbstractControl { return this.form.get(nombre)!; }
  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && c.touched;
  }

  guardar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;
    const vehiculo = this.vehiculo()!;
    const km = this.form.getRawValue().kilometrajeActual!;
    this.vehiculoService.actualizarKilometraje(vehiculo.id, { kilometrajeActual: km }).subscribe({
      next: () => this.guardado.emit(),
    });
  }
}