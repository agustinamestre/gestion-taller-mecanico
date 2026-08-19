import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import {
  FormBuilder,
  Validators,
  ReactiveFormsModule,
  AbstractControl,
} from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { VehiculoService } from '../../services/vehiculo.service';
import { MarcaService } from '../../../marcas/services/marca.service';
import { ModeloService } from '../../../modelos/services/modelo.service';
import { ClienteService } from '../../../clientes/services/cliente.service';
import { ClienteResponse } from '../../../clientes/models/cliente.model';
import { ModeloResponse } from '../../../modelos/models/modelo.model';
import { computed } from '@angular/core';
import { ClienteSelectorComponent } from '../../../../shared/components/cliente-selector/cliente-selector.component';

@Component({
  selector: 'app-vehiculo-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    InputTextModule,
    InputNumberModule,
    SelectModule,
    ButtonModule,
    ClienteSelectorComponent,
  ],
  templateUrl: './vehiculo-form.component.html',
  styleUrl: './vehiculo-form.component.scss',
})
export class VehiculoFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly vehiculoService = inject(VehiculoService);
  readonly marcaService = inject(MarcaService);
  readonly modeloService = inject(ModeloService);
  readonly clienteService = inject(ClienteService);

  readonly edicion = input(false);
  readonly clienteIdPreseteado = input<number | null>(null);
  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly enviado = signal(false);
  readonly marcaSeleccionadaId = signal<number | null>(null);
  readonly clienteIdParaSelector = signal<number | null>(null);

  readonly modelosFiltrados = computed(() => {
    const marcaId = this.marcaSeleccionadaId();
    return marcaId
      ? this.modeloService.modelos().filter((m) => m.marca.id === marcaId)
      : this.modeloService.modelos();
  });

  readonly anioActual = new Date().getFullYear();

  readonly form = this.fb.group({
    patente: ['', [Validators.required, Validators.pattern(/^[A-Z]{2}\d{3}[A-Z]{2}$|^[A-Z]{3}\d{3}$/)],],
    marcaId: [null as number | null, Validators.required],
    modeloId: [null as number | null, Validators.required],
    anio: [null as number | null, [Validators.required, Validators.min(1900), Validators.max(this.anioActual)]],
    clienteId: [null as number | null, Validators.required],
    kilometrajeActual: [null as number | null, [Validators.required, Validators.min(0)]],
  });

  ngOnInit() {
    this.form.get('modeloId')?.disable();
    this.marcaService.listar().subscribe();
    this.modeloService.listar().subscribe();

    if (this.clienteIdPreseteado()) {
      this.form.patchValue({ clienteId: this.clienteIdPreseteado() });
      this.clienteIdParaSelector.set(this.clienteIdPreseteado());
    }

    const vehiculo = this.vehiculoService.vehiculoActual();
    if (this.edicion() && vehiculo) {
      const marcaId = this.modeloService.modelos()
        .find((m) => m.id === vehiculo.modeloId)?.marca.id ?? null;

      this.marcaSeleccionadaId.set(marcaId);
      this.clienteIdParaSelector.set(vehiculo.cliente.id);

      this.form.patchValue({
        patente: vehiculo.patente,
        marcaId,
        modeloId: vehiculo.modeloId,
        anio: vehiculo.anio,
        clienteId: vehiculo.cliente.id,
        kilometrajeActual: vehiculo.kilometrajeActual,
      });

      this.form.get('patente')?.disable();
      this.form.get('kilometrajeActual')?.disable();
    }
  }

  onMarcaChange(marcaId: number | null) {
    this.marcaSeleccionadaId.set(marcaId);
    this.form.patchValue({ modeloId: null });

    const modeloControl = this.form.get('modeloId');
    marcaId ? modeloControl?.enable() : modeloControl?.disable();
  }

  onClienteSeleccionado(cliente: ClienteResponse | null) {
    const control = this.form.get('clienteId');
    control?.setValue(cliente?.id ?? null, { emitEvent: false });
    control?.markAsTouched();
  }

  campo(nombre: string): AbstractControl {
    return this.form.get(nombre)!;
  }

  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && (c.touched || this.enviado());
  }

  guardar() {
    this.enviado.set(true);
    if (this.form.invalid) return;

    const val = this.form.getRawValue();

    if (this.edicion()) {
      const vehiculo = this.vehiculoService.vehiculoActual()!;
      this.vehiculoService.modificar(vehiculo.id, {
        modeloId: val.modeloId!,
        anio: val.anio!,
        clienteId: val.clienteId!,
      }).subscribe({ next: () => this.guardado.emit() });
    } else {
      this.vehiculoService.registrar({
        patente: val.patente!,
        modeloId: val.modeloId!,
        anio: val.anio!,
        clienteId: val.clienteId!,
        kilometrajeActual: val.kilometrajeActual!,
      }).subscribe({ next: () => this.guardado.emit() });
    }
  }
}