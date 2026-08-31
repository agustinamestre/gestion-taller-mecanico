import { Component, inject, output, signal } from '@angular/core';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ProductoService } from '../../services/producto.service';
import { ProductoResponse, TipoProducto } from '../../models/producto.model';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, Location } from '@angular/common';
import { ToggleSwitchModule } from 'primeng/toggleswitch';

interface FiltroOpcion {
  label: string;
  value: TipoProducto | null;
}

@Component({
  selector: 'app-producto-table',
  standalone: true,
  imports: [CurrencyPipe, FormsModule, TableModule, ButtonModule, SelectButtonModule, ToggleSwitchModule],
  templateUrl: './producto-table.component.html',
  styleUrl: './producto-table.component.scss',
})
export class ProductoTableComponent {
  readonly productoService = inject(ProductoService);
  private readonly location = inject(Location);

  readonly nuevoProducto = output<void>();
  readonly verDetalle = output<ProductoResponse>();
  readonly editar = output<ProductoResponse>();
  readonly actualizarPrecio = output<ProductoResponse>();
  readonly actualizarStock = output<ProductoResponse>();

  readonly opcionesFiltro: FiltroOpcion[] = [
    { label: 'Todos', value: null },
    { label: 'Repuestos', value: 'REPUESTO' },
    { label: 'Mano de obra', value: 'MANO_DE_OBRA' },
  ];

  constructor() {
    this.productoService.listar().subscribe();
  }

  volver() {
    this.location.back();
  }

  formatearTipo(tipo: TipoProducto): string {
    return tipo === 'MANO_DE_OBRA' ? 'Mano de obra' : 'Repuesto';
  }

  onFiltroChange(tipo: TipoProducto | null) {
    this.productoService.filtroTipo.set(tipo);
  }

}