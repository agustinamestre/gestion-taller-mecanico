import { Component, computed, inject, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { SelectButtonModule } from 'primeng/selectbutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { PresupuestoService } from '../../services/presupuesto.service';
import { ProductoService } from '../../../productos/services/producto.service';
import { TipoProducto } from '../../../productos/models/producto.model';

@Component({
  selector: 'app-presupuesto-item-form',
  standalone: true,
  imports: [FormsModule, SelectModule, SelectButtonModule, InputNumberModule, InputTextModule, ButtonModule],
  templateUrl: './presupuesto-item-form.component.html',
  styleUrl: './presupuesto-item-form.component.scss',
})
export class PresupuestoItemFormComponent implements OnInit {
  readonly presupuestoService = inject(PresupuestoService);
  readonly productoService = inject(ProductoService);

  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly presupuesto = computed(() => this.presupuestoService.seleccionado());
  readonly itemEnEdicion = computed(() => this.presupuestoService.itemEnEdicion());

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
    const presupuesto = this.presupuesto();
    const productoId = this.productoIdSeleccionado();
    const descripcion = this.descripcion().trim();
    const cantidad = this.cantidad();
    const precio = this.precio();

    if (!presupuesto || this.invalido()) return;

    const item = this.itemEnEdicion();
    if (item) {
      this.presupuestoService.modificarItem(presupuesto.id, item.id, {
        productoId: productoId!, descripcion, cantidad: cantidad!, precioUnitario: precio!,
      }).subscribe({ next: () => this.guardado.emit() });
    } else {
      this.presupuestoService.agregarItem(presupuesto.id, {
        productoId: productoId!, descripcion, cantidad: cantidad!, precioUnitario: precio!,
      }).subscribe({ next: () => this.guardado.emit() });
    }
  }
}