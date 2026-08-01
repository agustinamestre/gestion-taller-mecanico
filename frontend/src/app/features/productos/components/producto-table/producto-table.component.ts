import { Component, inject, output, signal } from '@angular/core';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ProductoService } from '../../services/producto.service';
import { ProductoResponse, TipoProducto } from '../../models/producto.model';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { NotificationService } from '../../../../shared/services/notification.service';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { ToggleSwitchModule } from 'primeng/toggleswitch';

interface FiltroOpcion {
  label: string;
  value: TipoProducto | null;
}

@Component({
  selector: 'app-producto-table',
  standalone: true,
  imports: [CurrencyPipe, FormsModule, TableModule, ButtonModule, SelectButtonModule, ConfirmDialogComponent, ToggleSwitchModule],
  templateUrl: './producto-table.component.html',
  styleUrl: './producto-table.component.scss',
})
export class ProductoTableComponent {
  readonly productoService = inject(ProductoService);
  private readonly notification = inject(NotificationService);

  readonly nuevoProducto = output<void>();
  readonly verDetalle = output<ProductoResponse>();
  readonly editar = output<ProductoResponse>();

  readonly opcionesFiltro: FiltroOpcion[] = [
    { label: 'Todos', value: null },
    { label: 'Repuestos', value: 'REPUESTO' },
    { label: 'Mano de obra', value: 'MANO_DE_OBRA' },
  ];

  readonly mostrarConfirmBaja = signal(false);
  readonly productoABaja = signal<ProductoResponse | null>(null);

  constructor() {
    this.productoService.listar().subscribe();
  }

  formatearTipo(tipo: TipoProducto): string {
    return tipo === 'MANO_DE_OBRA' ? 'Mano de obra' : 'Repuesto';
  }

  onFiltroChange(tipo: TipoProducto | null) {
    this.productoService.filtroTipo.set(tipo);
  }

  pedirBaja(producto: ProductoResponse) {
    this.productoABaja.set(producto);
    this.mostrarConfirmBaja.set(true);
  }

  confirmarBaja() {
    const producto = this.productoABaja();
    if (!producto) return;
    this.productoService.desactivar(producto.id).subscribe({
      next: () => {
        this.notification.exito('Producto desactivado correctamente');
        this.cancelarBaja();
      },
    });
  }

  cancelarBaja() {
    this.mostrarConfirmBaja.set(false);
    this.productoABaja.set(null);
  }
}