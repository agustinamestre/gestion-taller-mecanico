import { Component, computed, effect, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TextareaModule } from 'primeng/textarea';
import { CheckboxModule } from 'primeng/checkbox';
import { OrdenTrabajoService } from '../../services/orden-trabajo.service';
import { ItemOrdenTrabajoResponse, ESTADOS_MODIFICABLES, ESTADO_ORDEN_LABELS, EstadoOrdenTrabajo } from '../../models/orden-trabajo.model';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { ItemFormComponent, ItemFormResultado } from '../../../../shared/components/item-form/item-form.component';
import { GenerarFacturaFormComponent } from '../../../facturas/components/generar-factura-form/generar-factura-form.component';
import { FacturaService } from '../../../facturas/services/factura.service';

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
    CurrencyPipe, DatePipe, DecimalPipe, FormsModule, ButtonModule, TableModule, TextareaModule, CheckboxModule,
    ItemFormComponent, ConfirmDialogComponent, GenerarFacturaFormComponent,
  ],
  templateUrl: './orden-trabajo-detail.component.html',
  styleUrl: './orden-trabajo-detail.component.scss',
})
export class OrdenTrabajoDetailComponent {
  readonly ordenTrabajoService = inject(OrdenTrabajoService);
  private readonly facturaService = inject(FacturaService);

  readonly volver = output<void>();
  readonly vista = signal<Vista>('detalle');
  readonly saldoPendiente = signal<number>(0);

  readonly mostrarConfirmEliminar = signal(false);
  readonly itemAEliminar = signal<ItemOrdenTrabajoResponse | null>(null);

  readonly editandoDescripcion = signal(false);
  readonly descripcionEditada = signal('');
  readonly tieneSeniaActiva = signal(false);

  constructor() {
    effect(() => {
      const id = this.orden?.id;
      if (id) {
        this.cargarEstadoSenia(id);
      } else {
        this.tieneSeniaActiva.set(false);
      }
    });
  }

  private cargarEstadoSenia(ordenId: number) {
    this.facturaService.obtenerActivasPorOrden(ordenId).subscribe({
      next: (facturas) => {
        const haySeniaEmitida = facturas.some(
          (f) => f.estado === 'EMITIDA' && f.tipoFactura === 'SENIA'
        );
        this.tieneSeniaActiva.set(haySeniaEmitida);
      },
    });
  }

  get orden() {
    return this.ordenTrabajoService.seleccionado();
  }

  get esModificable(): boolean {
    const estado = this.orden?.estado;
    return !!estado && ESTADOS_MODIFICABLES.includes(estado);
  }

  get puedeFacturarSenia(): boolean {
    const estado = this.orden?.estado;
    // Se permiten varias señas mientras quede saldo pendiente
    return (estado === 'INGRESADO' || estado === 'EN_REPARACION') && !this.orden?.facturada;
  }

  get estaCerrada(): boolean {
    const estado = this.orden?.estado;
    return estado === 'FINALIZADO' || estado === 'ENTREGADO';
  }

  get puedeFacturarFinal(): boolean {
    return this.estaCerrada && !this.orden?.facturada && (this.orden?.total ?? 0) > 0;
  }

  get esFacturable(): boolean {
    return this.puedeFacturarSenia || this.puedeFacturarFinal;
  }

  get labelBotonFacturar(): string {
    return this.puedeFacturarSenia && !this.puedeFacturarFinal ? 'Señar' : 'Facturar';
  }

  formatearEstado(estado: EstadoOrdenTrabajo): string {
    return ESTADO_ORDEN_LABELS[estado];
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

  cambiarIncluyeService(incluyeService: boolean) {
    const orden = this.orden;
    if (!orden) return;

    this.ordenTrabajoService.modificar(orden.id, {
      descripcionProblema: orden.descripcionProblema,
      incluyeService,
    }).subscribe();
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
    const orden = this.orden;
    if (!orden) return;

    this.saldoPendiente.set(orden.total);
    this.facturaService.obtenerActivasPorOrden(orden.id).subscribe({
      next: (facturas) => {
        const facturado = facturas
          .filter((f) => f.estado === 'EMITIDA')
          .reduce((acc, f) => acc + f.montoFacturado, 0);
        this.saldoPendiente.set(orden.total - facturado);
      },
    });

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
