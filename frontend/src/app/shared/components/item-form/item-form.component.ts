import { Component, computed, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { SelectButtonModule } from 'primeng/selectbutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ProductoService } from '../../../features/productos/services/producto.service';
import { TipoProducto } from '../../../features/productos/models/producto.model';

export interface ItemEditable {
  productoId: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
}

export interface ItemFormResultado {
  productoId: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
}

@Component({
  selector: 'app-item-form',
  standalone: true,
  imports: [FormsModule, SelectModule, SelectButtonModule, InputNumberModule, InputTextModule, ButtonModule],
  templateUrl: './item-form.component.html',
  styleUrl: './item-form.component.scss',
})
export class ItemFormComponent implements OnInit {
  readonly productoService = inject(ProductoService);

  readonly itemEnEdicion = input<ItemEditable | null>(null);
  readonly cargando = input(false);

  readonly guardado = output<ItemFormResultado>();
  readonly cancelar = output<void>();

  readonly filtroTipo = signal<TipoProducto | null>(null);
  readonly productoIdSeleccionado = signal<number | null>(null);
  readonly descripcion = signal('');
  readonly cantidad = signal<number | null>(null);
  readonly precio = signal<number | null>(null);

  readonly opcionesFiltro = [
    { label: 'Todos', value: null },
    { label: 'Repuestos', value: 'REPUESTO' as TipoProducto | null },
    { label: 'Mano de obra', value: 'MANO_DE_OBRA' as TipoProducto | null },
  ];

  readonly productosFiltrados = computed(() => {
    const tipo = this.filtroTipo();
    return this.productoService.productos().filter(p => tipo ? p.tipo === tipo : true);
  });

  ngOnInit() {
    if (this.productoService.productos().length === 0) {
      this.productoService.listar().subscribe();
    }

    const item = this.itemEnEdicion();
    if (item) {
      this.productoIdSeleccionado.set(item.productoId);
      this.descripcion.set(item.descripcion);
      this.cantidad.set(item.cantidad);
      this.precio.set(item.precioUnitario);
    } else {
      this.cantidad.set(1);
    }
  }

  onProductoChange(productoId: number | null) {
    this.productoIdSeleccionado.set(productoId);
    const producto = this.productoService.productos().find(p => p.id === productoId);
    if (producto) {
      this.descripcion.set(producto.descripcion);
      this.precio.set(producto.precioActual);
    }
  }

  invalido(): boolean {
    return !this.productoIdSeleccionado() || !this.descripcion().trim()
      || !this.cantidad() || this.cantidad()! <= 0
      || this.precio() == null || this.precio()! < 0;
  }

  guardar() {
    if (this.invalido()) return;

    this.guardado.emit({
      productoId: this.productoIdSeleccionado()!,
      descripcion: this.descripcion().trim(),
      cantidad: this.cantidad()!,
      precioUnitario: this.precio()!,
    });
  }
}