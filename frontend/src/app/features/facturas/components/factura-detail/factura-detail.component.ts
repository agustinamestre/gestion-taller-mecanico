import { Component, computed, effect, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { TextareaModule } from 'primeng/textarea';
import { FacturaService } from '../../services/factura.service';
import { FormaPago, FORMA_PAGO_LABELS } from '../../models/factura.model';
import { AnularFacturaFormComponent } from '../anular-factura-form/anular-factura-form.component';

type Vista = 'detalle' | 'anular-form';

interface ItemFacturaUnificado {
  id: number;
  descripcion: string;
  tipo: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  origen: 'presupuesto' | 'orden';
}

@Component({
  selector: 'app-factura-detail',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, ButtonModule, TableModule, AnularFacturaFormComponent],
  templateUrl: './factura-detail.component.html',
  styleUrl: './factura-detail.component.scss',
})
export class FacturaDetailComponent {
  readonly facturaService = inject(FacturaService);

  readonly volver = output<void>();
  readonly vista = signal<Vista>('detalle');
  readonly tieneFinalEmitida = signal(false);

  constructor() {
    effect(() => {
      const f = this.factura;
      if (f && f.tipoFactura === 'SENIA' && f.estado === 'EMITIDA') {
        this.chequearFinalEmitida(f.ordenTrabajo.id);
      } else {
        this.tieneFinalEmitida.set(false);
      }
    });
  }

  get factura() {
    return this.facturaService.seleccionada();
  }

  get puedeAnular(): boolean {
    const f = this.factura;
    if (!f || f.estado !== 'EMITIDA') return false;
    if (f.ordenTrabajo.estado === 'ENTREGADO') return false;
    if (f.tipoFactura === 'SENIA' && this.tieneFinalEmitida()) return false;
    return true;
  }

  abrirAnular() {
    this.vista.set('anular-form');
  }

  onAnulada() {
    this.vista.set('detalle');
  }

  irADetalle() {
    this.vista.set('detalle');
  }

  imprimir() {
    window.print();
  }

  formatearTipo(tipo: string): string {
    return tipo === 'MANO_DE_OBRA' ? 'Mano de obra' : 'Repuesto';
  }

  formatearFormaPago(formaPago: FormaPago): string {
    return FORMA_PAGO_LABELS[formaPago];
  }

  readonly itemsUnificados = computed<ItemFacturaUnificado[]>(() => {
    const orden = this.factura?.ordenTrabajo;
    if (!orden) return [];

    const deLosPresupuesto: ItemFacturaUnificado[] = (orden.itemsPresupuesto ?? []).map(item => ({
      id: item.id, descripcion: item.descripcion, tipo: item.tipo,
      cantidad: item.cantidad, precioUnitario: item.precioUnitario, subtotal: item.subtotal,
      origen: 'presupuesto',
    }));

    const deLaOrden: ItemFacturaUnificado[] = (orden.itemsOrden ?? []).map(item => ({
      id: item.id, descripcion: item.descripcion, tipo: item.tipo,
      cantidad: item.cantidad, precioUnitario: item.precioUnitario, subtotal: item.subtotal,
      origen: 'orden',
    }));

    return [...deLosPresupuesto, ...deLaOrden];
  });

  private chequearFinalEmitida(ordenTrabajoId: number) {
    this.facturaService.obtenerActivasPorOrden(ordenTrabajoId).subscribe({
      next: (facturas) => {
        const hayFinalEmitida = facturas.some(
          (f) => f.estado === 'EMITIDA' && f.tipoFactura === 'FINAL'
        );
        this.tieneFinalEmitida.set(hayFinalEmitida);
      },
    });
  }
}
