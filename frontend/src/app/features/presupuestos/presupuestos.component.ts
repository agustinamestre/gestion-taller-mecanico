import { Component, inject, signal } from '@angular/core';
import { PresupuestoService } from './services/presupuesto.service';
import { PresupuestoTableComponent } from './components/presupuesto-table/presupuesto-table.component';
import { PresupuestoFormComponent } from './components/presupuesto-form/presupuesto-form.component';
import { PresupuestoDetailComponent } from './components/presupuesto-detail/presupuesto-detail.component';

type Vista = 'busqueda' | 'alta' | 'detalle';

@Component({
  selector: 'app-presupuestos',
  standalone: true,
  imports: [PresupuestoTableComponent, PresupuestoFormComponent, PresupuestoDetailComponent],
  templateUrl: './presupuestos.component.html',
  styleUrl: './presupuestos.component.scss',
})
export class PresupuestosComponent {
  readonly presupuestoService = inject(PresupuestoService);
  readonly vista = signal<Vista>('busqueda');
  readonly patenteActual = signal<string | null>(null);

  irABusqueda() {
    this.presupuestoService.limpiarSeleccion();
    this.vista.set('busqueda');
  }

  irAAlta(patente: string | null) {
    this.patenteActual.set(patente);
    this.vista.set('alta');
  }

  irADetalle(id: number) {
    this.presupuestoService.obtener(id).subscribe({
      next: () => this.vista.set('detalle'),
    });
  }

  onCreado(id: number) {
    this.irADetalle(id);
  }
}