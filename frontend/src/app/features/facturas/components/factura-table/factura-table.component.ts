import { Component, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { FacturaService } from '../../services/factura.service';

@Component({
  selector: 'app-factura-table',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, InputTextModule, ButtonModule, TableModule],
  templateUrl: './factura-table.component.html',
  styleUrl: './factura-table.component.scss',
})
export class FacturaTableComponent {
  readonly facturaService = inject(FacturaService);
  private readonly route = inject(ActivatedRoute);

  readonly verDetalle = output<number>();

  readonly numeroFactura = signal('');
  readonly clienteDni = signal('');
  readonly patenteVehiculo = signal('');

  constructor() {
    const params = this.route.snapshot.queryParamMap;
    const desde = params.get('desde') ?? undefined;
    const hasta = params.get('hasta') ?? undefined;

    this.facturaService.listar({ desde, hasta }).subscribe();
  }

  buscar() {
    this.facturaService.listar({
      numeroFactura: this.numeroFactura().trim() || undefined,
      clienteDni: this.clienteDni().trim() || undefined,
      patenteVehiculo: this.patenteVehiculo().trim().toUpperCase() || undefined,
    }).subscribe();
  }

  limpiarFiltros() {
    this.numeroFactura.set('');
    this.clienteDni.set('');
    this.patenteVehiculo.set('');
    this.facturaService.listar().subscribe();
  }

  get tieneFiltrosActivos(): boolean {
    return !!this.numeroFactura().trim() || !!this.clienteDni().trim() || !!this.patenteVehiculo().trim();
  }
}