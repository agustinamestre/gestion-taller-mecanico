import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { PresupuestoService } from '../../services/presupuesto.service';
import { VehiculoService } from '../../../vehiculos/services/vehiculo.service';
import { VehiculoResponse } from '../../../vehiculos/models/vehiculo.model';
import { VehiculoSelectorComponent } from '../../../../shared/components/vehiculo-selector/vehiculo-selector.component';

@Component({
  selector: 'app-presupuesto-form',
  standalone: true,
  imports: [ReactiveFormsModule, TextareaModule, ButtonModule, VehiculoSelectorComponent],
  templateUrl: './presupuesto-form.component.html',
  styleUrl: './presupuesto-form.component.scss',
})
export class PresupuestoFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly presupuestoService = inject(PresupuestoService);
  readonly vehiculoService = inject(VehiculoService);

  readonly patente = input<string | null>(null);
  readonly creado = output<number>();
  readonly cancelar = output<void>();

  readonly buscandoVehiculo = signal(false);
  readonly vehiculoNoEncontrado = signal(false);
  readonly vehiculoElegido = signal<VehiculoResponse | null>(null);

  readonly form = this.fb.group({
    observaciones: ['', Validators.maxLength(500)],
  });

  ngOnInit() {
    const patente = this.patente();
    if (!patente) return;

    this.buscandoVehiculo.set(true);
    this.vehiculoService.buscarPorPatente(patente).subscribe({
      next: (vehiculo) => {
        this.buscandoVehiculo.set(false);
        this.vehiculoElegido.set(vehiculo);
      },
      error: () => {
        this.buscandoVehiculo.set(false);
        this.vehiculoNoEncontrado.set(true);
      },
    });
  }

  onVehiculoSeleccionado(vehiculo: VehiculoResponse | null) {
    this.vehiculoElegido.set(vehiculo);
  }

  quitarVehiculo() {
    this.vehiculoElegido.set(null);
  }

  guardar() {
    if (this.form.invalid) return;
    const vehiculo = this.vehiculoElegido();

    this.presupuestoService.registrar({
      vehiculoId: vehiculo ? vehiculo.id : null,
      observaciones: this.form.value.observaciones || undefined,
    }).subscribe({
      next: (presupuesto) => this.creado.emit(presupuesto.id),
    });
  }
}