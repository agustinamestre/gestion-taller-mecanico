import { Component, computed, inject, signal, viewChild, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { PresupuestoService } from '../../services/presupuesto.service';
import {
  AsociarVehiculoAPresupuestoRequest,
  AsociarVehiculoDatosClienteNuevo,
  AsociarVehiculoDatosVehiculoNuevo,
} from '../../models/presupuesto.model';
import { VehiculoSelectorComponent } from '../../../../shared/components/vehiculo-selector/vehiculo-selector.component';
import { ClienteSelectorComponent } from '../../../../shared/components/cliente-selector/cliente-selector.component';
import { ClienteFormComponent } from '../../../clientes/components/cliente-form/cliente-form.component';
import { VehiculoFormComponent } from '../../../vehiculos/components/vehiculo-form/vehiculo-form.component';
import { VehiculoResponse, VehiculoRequest } from '../../../vehiculos/models/vehiculo.model';
import { ClienteResponse, ClienteRequest } from '../../../clientes/models/cliente.model';

type ModoVehiculo = 'existente' | 'nuevo';
type ModoCliente = 'existente' | 'nuevo';

@Component({
  selector: 'app-presupuesto-vehiculo-form',
  standalone: true,
  imports: [
    FormsModule, ButtonModule, SelectButtonModule,
    VehiculoSelectorComponent, ClienteSelectorComponent,
    ClienteFormComponent, VehiculoFormComponent,
  ],
  templateUrl: './presupuesto-vehiculo-form.component.html',
  styleUrl: './presupuesto-vehiculo-form.component.scss',
})
export class PresupuestoVehiculoFormComponent {
  readonly presupuestoService = inject(PresupuestoService);

  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly clienteFormRef = viewChild(ClienteFormComponent);
  readonly vehiculoFormRef = viewChild(VehiculoFormComponent);

  readonly modoVehiculo = signal<ModoVehiculo | null>(null);
  readonly modoCliente = signal<ModoCliente | null>(null);

  readonly opcionesModoVehiculo = [
    { label: 'Vehículo existente', value: 'existente' as ModoVehiculo },
    { label: 'Vehículo nuevo', value: 'nuevo' as ModoVehiculo },
  ];

  readonly opcionesModoCliente = [
    { label: 'Cliente existente', value: 'existente' as ModoCliente },
    { label: 'Cliente nuevo', value: 'nuevo' as ModoCliente },
  ];

  readonly vehiculoExistenteElegido = signal<VehiculoResponse | null>(null);
  readonly clienteExistenteElegido = signal<ClienteResponse | null>(null);
  readonly datosClienteNuevo = signal<AsociarVehiculoDatosClienteNuevo | null>(null);
  readonly datosVehiculoNuevo = signal<AsociarVehiculoDatosVehiculoNuevo | null>(null);

  readonly clienteResuelto = computed(() =>
    this.modoCliente() === 'existente' ? this.clienteExistenteElegido() != null : this.datosClienteNuevo() != null
  );

  readonly clienteIdParaVehiculoNuevo = computed(() => this.clienteExistenteElegido()?.id ?? null);

  readonly listoParaConfirmar = computed(() => {
    if (this.modoVehiculo() === 'existente') {
      return this.vehiculoExistenteElegido() != null;
    }
    if (this.modoVehiculo() === 'nuevo') {
      return this.clienteResuelto() && this.datosVehiculoNuevo() != null;
    }
    return false;
  });

  readonly labelBoton = computed(() => {
    if (this.modoVehiculo() === 'nuevo' && this.modoCliente() === 'nuevo' && !this.datosClienteNuevo()) {
      return 'Guardar cliente';
    }
    if (this.modoVehiculo() === 'nuevo' && !this.datosVehiculoNuevo()) {
      return 'Guardar vehículo';
    }
    return 'Asociar vehículo';
  });

  elegirModoVehiculo(modo: ModoVehiculo) {
    this.modoVehiculo.set(modo);
    this.vehiculoExistenteElegido.set(null);
    this.modoCliente.set(null);
    this.clienteExistenteElegido.set(null);
    this.datosClienteNuevo.set(null);
    this.datosVehiculoNuevo.set(null);
  }

  elegirModoCliente(modo: ModoCliente) {
    this.modoCliente.set(modo);
    this.clienteExistenteElegido.set(null);
    this.datosClienteNuevo.set(null);
    this.datosVehiculoNuevo.set(null);
  }

  onVehiculoExistente(vehiculo: VehiculoResponse | null) {
    this.vehiculoExistenteElegido.set(vehiculo);
  }

  onClienteExistente(cliente: ClienteResponse | null) {
    this.clienteExistenteElegido.set(cliente);
  }

  onDatosClienteListos(datos: ClienteRequest) {
    this.datosClienteNuevo.set(datos);
  }

  onDatosVehiculoListos(datos: VehiculoRequest) {
    this.datosVehiculoNuevo.set({
      patente: datos.patente,
      modeloId: datos.modeloId,
      anio: datos.anio,
      kilometrajeActual: datos.kilometrajeActual,
    });
  }

  confirmar() {
    const presupuestoId = this.presupuestoService.seleccionado()?.id;
    if (!presupuestoId) return;

    if (this.modoVehiculo() === 'nuevo' && this.modoCliente() === 'nuevo' && !this.datosClienteNuevo()) {
      this.clienteFormRef()?.guardar();
      if (!this.datosClienteNuevo()) return;
    }

    if (this.modoVehiculo() === 'nuevo' && !this.datosVehiculoNuevo()) {
      this.vehiculoFormRef()?.guardar();
      if (!this.datosVehiculoNuevo()) return;
    }

    if (!this.listoParaConfirmar()) return;

    let request: AsociarVehiculoAPresupuestoRequest;

    if (this.modoVehiculo() === 'existente') {
      request = { vehiculoId: this.vehiculoExistenteElegido()!.id };
    } else {
      request = {
        datosVehiculoNuevo: this.datosVehiculoNuevo()!,
        ...(this.modoCliente() === 'existente'
          ? { clienteId: this.clienteExistenteElegido()!.id }
          : { datosClienteNuevo: this.datosClienteNuevo()! }),
      };
    }

    this.presupuestoService.asociarVehiculo(presupuestoId, request).subscribe({
      next: () => this.guardado.emit(),
    });
  }
}