import { Component, inject, signal } from '@angular/core';
import { OrdenTrabajoService } from './services/orden-trabajo.service';
import { OrdenTrabajoFormComponent } from './components/orden-trabajo-form/orden-trabajo-form.component';
import { OrdenTrabajoTableComponent } from './components/orden-trabajo-table/orden-trabajo-table.component';
import { OrdenTrabajoDetailComponent } from './components/orden-trabajo-detail/orden-trabajo-detail.component';

type Vista = 'busqueda' | 'alta' | 'detalle';

@Component({
  selector: 'app-ordenes-trabajo',
  standalone: true,
  imports: [OrdenTrabajoTableComponent, OrdenTrabajoFormComponent, OrdenTrabajoDetailComponent],
  templateUrl: './ordenes-trabajo.component.html',
  styleUrl: './ordenes-trabajo.component.scss',
})
export class OrdenesTrabajoComponent {
  readonly ordenTrabajoService = inject(OrdenTrabajoService);
  readonly vista = signal<Vista>('busqueda');
  readonly patenteActual = signal<string | null>(null);

  irABusqueda() {
    this.ordenTrabajoService.limpiarSeleccion();
    this.vista.set('busqueda');
  }

  irAAlta() {
    this.vista.set('alta');
  }

  irADetalle(id: number) {
    this.ordenTrabajoService.obtener(id).subscribe({
      next: () => this.vista.set('detalle'),
    });
  }

  onCreada(id: number) {
    this.irADetalle(id);
  }
}