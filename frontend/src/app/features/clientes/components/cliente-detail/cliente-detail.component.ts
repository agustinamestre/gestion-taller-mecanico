import { Component, inject, output } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ClienteService } from '../../services/cliente.service';
import { ClienteResponse } from '../../models/cliente.model';
import { VehiculoService } from '../../../vehiculos/services/vehiculo.service';

@Component({
  selector: 'app-cliente-detail',
  standalone: true,
  imports: [DatePipe, DecimalPipe, ButtonModule, TableModule],
  templateUrl: './cliente-detail.component.html',
  styleUrl: './cliente-detail.component.scss',
})
export class ClienteDetailComponent {
  readonly clienteService = inject(ClienteService);
  private readonly vehiculoService = inject(VehiculoService);
  private readonly router = inject(Router);

  readonly editar = output<ClienteResponse>();
  readonly volver = output<void>();

  get cliente() {
    return this.clienteService.clienteSeleccionado();
  }

  formatearSituacionIva(situacion: string): string {
    const mapa: Record<string, string> = {
      RESPONSABLE_INSCRIPTO: 'Responsable Inscripto',
      MONOTRIBUTISTA: 'Monotributista',
      CONSUMIDOR_FINAL: 'Consumidor Final',
      EXENTO: 'Exento',
    };
    return mapa[situacion] ?? situacion;
  }

  verVehiculo(patente: string) {
    this.vehiculoService.buscarPorPatente(patente).subscribe({
      next: () => this.router.navigate(['/vehiculos']),
    });
  }

  registrarVehiculo() {
    this.router.navigate(['/vehiculos'], {
      state: { clienteId: this.cliente?.id }
    });
  }
}