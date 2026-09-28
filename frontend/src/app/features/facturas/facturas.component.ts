import { Component, inject, signal } from '@angular/core';
import { FacturaService } from './services/factura.service';
import { FacturaTableComponent } from './components/factura-table/factura-table.component';
import { FacturaDetailComponent } from './components/factura-detail/factura-detail.component';

type Vista = 'listado' | 'detalle';

@Component({
  selector: 'app-facturas',
  standalone: true,
  imports: [FacturaTableComponent, FacturaDetailComponent],
  templateUrl: './facturas.component.html',
  styleUrl: './facturas.component.scss',
})
export class FacturasComponent {
  readonly facturaService = inject(FacturaService);
  readonly vista = signal<Vista>('listado');

  irAListado() {
    this.facturaService.limpiarSeleccion();
    this.vista.set('listado');
  }

  irADetalle(id: number) {
    this.facturaService.obtener(id).subscribe({
      next: () => this.vista.set('detalle'),
    });
  }
}
