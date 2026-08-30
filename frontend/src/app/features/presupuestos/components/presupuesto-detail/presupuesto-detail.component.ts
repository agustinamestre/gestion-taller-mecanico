import { Component, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { PresupuestoService } from '../../services/presupuesto.service';
import { ItemPresupuestoResponse } from '../../models/presupuesto.model';
import { PresupuestoItemFormComponent } from '../presupuesto-item-form/presupuesto-item-form.component';
import { PresupuestoCambiarEstadoComponent } from '../presupuesto-cambiar-estado/presupuesto-cambiar-estado.component';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { PresupuestoVehiculoFormComponent } from '../presupuesto-vehiculo-form/presupuesto-vehiculo-form.component';

type Vista = 'detalle' | 'item-form' | 'estado-form' | 'vehiculo-form';

@Component({
  selector: 'app-presupuesto-detail',
  standalone: true,
  imports: [
    CurrencyPipe, DatePipe, ButtonModule, TableModule,
    PresupuestoItemFormComponent, PresupuestoCambiarEstadoComponent,
    PresupuestoVehiculoFormComponent, ConfirmDialogComponent,
  ],
  templateUrl: './presupuesto-detail.component.html',
  styleUrl: './presupuesto-detail.component.scss',
})
export class PresupuestoDetailComponent {
  readonly presupuestoService = inject(PresupuestoService);

  readonly volver = output<void>();
  readonly vista = signal<Vista>('detalle');

  readonly mostrarConfirmEliminar = signal(false);
  readonly itemAEliminar = signal<ItemPresupuestoResponse | null>(null);

  get presupuesto() {
    return this.presupuestoService.seleccionado();
  }

  get esEditable(): boolean {
    return this.presupuesto?.estado === 'PENDIENTE';
  }

  private refrescar() {
    const id = this.presupuesto?.id;
    if (id) this.presupuestoService.obtener(id).subscribe();
  }

  irADetalle() {
    this.presupuestoService.seleccionarItem(null);
    this.vista.set('detalle');
  }

  abrirNuevoItem() {
    this.presupuestoService.seleccionarItem(null);
    this.vista.set('item-form');
  }

  abrirEditarItem(item: ItemPresupuestoResponse) {
    this.presupuestoService.seleccionarItem(item);
    this.vista.set('item-form');
  }

  onItemGuardado() {
    this.refrescar();
    this.irADetalle();
  }

  formatearTipo(tipo: string): string {
    return tipo === 'MANO_DE_OBRA' ? 'Mano de obra' : 'Repuesto';
  }

  abrirCambiarEstado() {
    this.vista.set('estado-form');
  }

  onEstadoGuardado() {
    this.refrescar();
    this.irADetalle();
  }

  abrirAsociarVehiculo() {
    this.vista.set('vehiculo-form');
  }

  onVehiculoAsociado() {
    this.refrescar();
    this.irADetalle();
  }

  pedirEliminarItem(item: ItemPresupuestoResponse) {
    this.itemAEliminar.set(item);
    this.mostrarConfirmEliminar.set(true);
  }

  confirmarEliminarItem() {
    const presupuestoId = this.presupuesto?.id;
    const item = this.itemAEliminar();
    if (!presupuestoId || !item) return;
    this.presupuestoService.eliminarItem(presupuestoId, item.id).subscribe({
      next: () => {
        this.refrescar();
        this.cancelarEliminarItem();
      },
    });
  }

  cancelarEliminarItem() {
    this.mostrarConfirmEliminar.set(false);
    this.itemAEliminar.set(null);
  }
}