import { Component, inject, OnInit, output } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { AlertaService } from '../../services/alerta.service';
import { VehiculoService } from '../../../vehiculos/services/vehiculo.service';

@Component({
  selector: 'app-alerta-detail',
  standalone: true,
  imports: [DatePipe, DecimalPipe, ButtonModule],
  templateUrl: './alerta-detail.component.html',
  styleUrl: './alerta-detail.component.scss',
})
export class AlertaDetailComponent implements OnInit {
  readonly alertaService = inject(AlertaService);
  readonly vehiculoService = inject(VehiculoService);

  readonly volver = output<void>();

  get alerta() {
    return this.alertaService.alertaSeleccionada();
  }

  get vehiculo() {
    return this.vehiculoService.vehiculoActual();
  }

  ngOnInit() {
    const alerta = this.alerta;
    if (alerta) {
      this.vehiculoService.buscarPorPatente(alerta.patenteVehiculo).subscribe();
    }
  }
}
