import { Component, computed, inject, output } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { TextareaModule } from 'primeng/textarea';
import { FacturaService } from '../../services/factura.service';
import { FormaPago, FORMA_PAGO_LABELS } from '../../models/factura.model';

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
  imports: [CurrencyPipe, DatePipe, FormsModule, ButtonModule, TableModule],
  templateUrl: './factura-detail.component.html',
  styleUrl: './factura-detail.component.scss',
})
export class FacturaDetailComponent {
  readonly facturaService = inject(FacturaService);

  readonly volver = output<void>();

  get factura() {
    return this.facturaService.seleccionada();
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
}
