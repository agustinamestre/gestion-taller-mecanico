import { Component, computed, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { CheckboxModule } from 'primeng/checkbox';
import { InputNumberModule } from 'primeng/inputnumber';
import { OrdenTrabajoService } from '../../services/orden-trabajo.service';
import { RegistrarOrdenTrabajoRequest } from '../../models/orden-trabajo.model';
import { PresupuestoSelectorComponent } from '../../../../shared/components/presupuesto-selector/presupuesto-selector.component';
import { PresupuestoSummaryResponse } from '../../../presupuestos/models/presupuesto.model';
import { VehiculoSelectorComponent } from '../../../../shared/components/vehiculo-selector/vehiculo-selector.component';
import { VehiculoResponse } from '../../../vehiculos/models/vehiculo.model';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { PresupuestoService } from '../../../presupuestos/services/presupuesto.service';
import { VehiculoService } from '../../../vehiculos/services/vehiculo.service';

type ModoOrigen = 'presupuesto' | 'directa';

@Component({
  selector: 'app-orden-trabajo-form',
  standalone: true,
  imports: [FormsModule, ButtonModule, SelectButtonModule, SelectModule, TextareaModule, CheckboxModule, InputNumberModule,
    PresupuestoSelectorComponent, VehiculoSelectorComponent, CurrencyPipe, DecimalPipe
  ],
  templateUrl: './orden-trabajo-form.component.html',
  styleUrl: './orden-trabajo-form.component.scss',
})
export class OrdenTrabajoFormComponent {
  private readonly ordenTrabajoService = inject(OrdenTrabajoService);
  private readonly presupuestoService = inject(PresupuestoService);
  private readonly vehiculoService = inject(VehiculoService);

  readonly creada = output<number>();
  readonly cancelar = output<void>();

  readonly modoOrigen = signal<ModoOrigen | null>(null);

  readonly opcionesModoOrigen = [
    { label: 'Desde presupuesto', value: 'presupuesto' as ModoOrigen },
    { label: 'Sin presupuesto', value: 'directa' as ModoOrigen },
  ];

  readonly presupuestoId = signal<number | null>(null);
  readonly presupuestoElegido = signal<PresupuestoSummaryResponse | null>(null);
  readonly vehiculoElegido = signal<VehiculoResponse | null>(null);
  readonly observacionesPresupuesto = signal<string | null>(null);
  readonly descripcionProblema = signal('');
  readonly incluyeService = signal(false);
  readonly kilometrajeIngreso = signal<number | null>(null);
  readonly kmActualVehiculoPresupuesto = signal<number | null>(null);

  readonly kmActualVehiculo = computed(() =>
    this.modoOrigen() === 'presupuesto'
      ? this.kmActualVehiculoPresupuesto()
      : this.vehiculoElegido()?.kilometrajeActual ?? null
  );

  readonly errorKilometraje = computed<string | null>(() => {
    const km = this.kilometrajeIngreso();
    const kmActual = this.kmActualVehiculo();
    if (km != null && kmActual != null && km < kmActual) {
      return `No puede ser menor al kilometraje actual (${kmActual.toLocaleString('es-AR')} km).`;
    }
    return null;
  });

  readonly listoParaGuardar = computed(() => {
    const origenValido = this.modoOrigen() === 'presupuesto'
      ? this.presupuestoId() != null
      : this.vehiculoElegido() != null;

    const necesitaDescripcion = this.modoOrigen() === 'directa'
      || (this.modoOrigen() === 'presupuesto' && !this.observacionesPresupuesto());

    const descripcionValida = necesitaDescripcion
      ? this.descripcionProblema().trim().length > 0
      : true;

    return origenValido && descripcionValida && !this.errorKilometraje();
  });

  elegirModoOrigen(modo: ModoOrigen) {
    this.modoOrigen.set(modo);
    this.presupuestoId.set(null);
    this.presupuestoElegido.set(null);
    this.observacionesPresupuesto.set(null);
    this.vehiculoElegido.set(null);
    this.descripcionProblema.set('');
    this.incluyeService.set(false);
    this.kilometrajeIngreso.set(null);
    this.kmActualVehiculoPresupuesto.set(null);
  }

  onPresupuestoSeleccionado(presupuesto: PresupuestoSummaryResponse | null) {
    this.presupuestoElegido.set(presupuesto);
    this.presupuestoId.set(presupuesto?.id ?? null);
    this.observacionesPresupuesto.set(null);
    this.kmActualVehiculoPresupuesto.set(null);

    if (presupuesto) {
      this.presupuestoService.obtener(presupuesto.id).subscribe({
        next: (detalle) => this.observacionesPresupuesto.set(detalle.observaciones || null),
      });
    }

    const patente = presupuesto?.patenteVehiculo;
    if (patente) {
      this.vehiculoService.buscar(patente).subscribe({
        next: (vehiculos) => {
          const vehiculo = vehiculos.find((v) => v.patente === patente);
          if (this.presupuestoElegido()?.id === presupuesto!.id) {
            this.kmActualVehiculoPresupuesto.set(vehiculo?.kilometrajeActual ?? null);
          }
        },
      });
    }
  }

  onVehiculoSeleccionado(vehiculo: VehiculoResponse | null) {
    this.vehiculoElegido.set(vehiculo);
  }

  guardar() {
      if (!this.listoParaGuardar()) return;

      const request: RegistrarOrdenTrabajoRequest = {
        incluyeService: this.incluyeService(),
        ...(this.kilometrajeIngreso() != null ? { kilometrajeIngreso: this.kilometrajeIngreso()! } : {}),
        ...(this.modoOrigen() === 'presupuesto'
          ? {
              presupuestoId: this.presupuestoId()!,
              ...(this.descripcionProblema().trim() ? { descripcionProblema: this.descripcionProblema().trim() } : {}),
            }
          : { patente: this.vehiculoElegido()!.patente, descripcionProblema: this.descripcionProblema() }),
      };

      this.ordenTrabajoService.registrar(request).subscribe({
        next: (orden) => this.creada.emit(orden.id),
      });
    }
}