import { Component, computed, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TextareaModule } from 'primeng/textarea';
import { OrdenTrabajoService } from '../../services/orden-trabajo.service';
import { ItemOrdenTrabajoResponse, ESTADOS_MODIFICABLES } from '../../models/orden-trabajo.model';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { ItemFormComponent, ItemFormResultado } from '../../../../shared/components/item-form/item-form.component';
import { GenerarFacturaFormComponent } from '../../../facturas/components/generar-factura-form/generar-factura-form.component';

type Vista = 'detalle' | 'item-form' | 'generar-factura';
type OrigenItem = 'presupuesto' | 'orden';

interface ItemUnificado {
  id: number;
  descripcion: string;
  tipo: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  origen: OrigenItem;
  itemOriginal: ItemOrdenTrabajoResponse | null;
}

@Component({
  selector: 'app-orden-trabajo-detail',
  standalone: true,
  imports: [
    CurrencyPipe, DatePipe, FormsModule, ButtonModule, TableModule, TextareaModule,
    ItemFormComponent, ConfirmDialogComponent, GenerarFacturaFormComponent,
  ],
  templateUrl: './orden-trabajo-detail.component.html',
  styleUrl: './orden-trabajo-detail.component.scss',
})
export class OrdenTrabajoDetailComponent {
  readonly ordenTrabajoService = inject(OrdenTrabajoService);

  readonly volver = output<void>();
  readonly vista = signal<Vista>('detalle');

  readonly mostrarConfirmEliminar = signal(false);
  readonly itemAEliminar = signal<ItemOrdenTrabajoResponse | null>(null);

  readonly editandoDescripcion = signal(false);
  readonly descripcionEditada = signal('');

  get orden() {
    return this.ordenTrabajoService.seleccionado();
  }

  get esModificable(): boolean {
    const estado = this.orden?.estado;
    return !!estado && ESTADOS_MODIFICABLES.includes(estado);
  }

  get esFacturable(): boolean {
    const orden = this.orden;
    if (!orden) return false;
    return (orden.estado === 'FINALIZADO' || orden.estado === 'ENTREGADO') && !orden.facturada;
  }

  readonly itemsUnificados = computed<ItemUnificado[]>(() => {
    const o = this.orden;
    if (!o) return [];

    const deLosPresupuesto: ItemUnificado[] = (o.itemsPresupuesto ?? []).map(item => ({
      id: item.id, descripcion: item.descripcion, tipo: item.tipo,
      cantidad: item.cantidad, precioUnitario: item.precioUnitario, subtotal: item.subtotal,
      origen: 'presupuesto', itemOriginal: null,
    }));

    const deLaOrden: ItemUnificado[] = (o.itemsOrden ?? []).map(item => ({
      id: item.id, descripcion: item.descripcion, tipo: item.tipo,
      cantidad: item.cantidad, precioUnitario: item.precioUnitario, subtotal: item.subtotal,
      origen: 'orden', itemOriginal: item,
    }));

    return [...deLosPresupuesto, ...deLaOrden];
  });

  private refrescar() {
    const id = this.orden?.id;
    if (id) this.ordenTrabajoService.obtener(id).subscribe();
  }

  irADetalle() {
    this.ordenTrabajoService.seleccionarItem(null);
    this.vista.set('detalle');
  }

  abrirNuevoItem() {
    this.ordenTrabajoService.seleccionarItem(null);
    this.vista.set('item-form');
  }

  abrirEditarItem(item: ItemOrdenTrabajoResponse) {
    this.ordenTrabajoService.seleccionarItem(item);
    this.vista.set('item-form');
  }

  onGuardarItem(datos: ItemFormResultado) {
    const orden = this.orden;
    if (!orden) return;

    const item = this.ordenTrabajoService.itemEnEdicion();
    const request$ = item
      ? this.ordenTrabajoService.modificarItem(orden.id, item.id, datos)
      : this.ordenTrabajoService.agregarItem(orden.id, datos);

    request$.subscribe({ next: () => this.onItemGuardado() });
  }

  onItemGuardado() {
    this.refrescar();
    this.irADetalle();
  }

  formatearTipo(tipo: string): string {
    return tipo === 'MANO_DE_OBRA' ? 'Mano de obra' : 'Repuesto';
  }

  iniciarEdicionDescripcion() {
    this.descripcionEditada.set(this.orden?.descripcionProblema ?? '');
    this.editandoDescripcion.set(true);
  }

  cancelarEdicionDescripcion() {
    this.editandoDescripcion.set(false);
    this.descripcionEditada.set('');
  }

  guardarDescripcion() {
    const ordenId = this.orden?.id;
    const nuevaDescripcion = this.descripcionEditada().trim();
    if (!ordenId || !nuevaDescripcion) return;

    this.ordenTrabajoService.modificar(ordenId, { descripcionProblema: nuevaDescripcion }).subscribe({
      next: () => {
        this.editandoDescripcion.set(false);
        this.descripcionEditada.set('');
      },
    });
  }

  pedirEliminarItem(item: ItemOrdenTrabajoResponse) {
    this.itemAEliminar.set(item);
    this.mostrarConfirmEliminar.set(true);
  }

  confirmarEliminarItem() {
    const ordenId = this.orden?.id;
    const item = this.itemAEliminar();
    if (!ordenId || !item) return;
    this.ordenTrabajoService.eliminarItem(ordenId, item.id).subscribe({
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

  abrirGenerarFactura() {
    this.vista.set('generar-factura');
  }

  cancelarGenerarFactura() {
    this.vista.set('detalle');
  }

  onFacturaGenerada() {
    this.refrescar();
    this.vista.set('detalle');
  }
}