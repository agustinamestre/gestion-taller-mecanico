import { Component, inject, signal } from '@angular/core';
import { ProductoService } from './services/producto.service';
import { ProductoResponse } from './models/producto.model';
import { ProductoTableComponent } from './components/producto-table/producto-table.component';
import { ProductoFormComponent } from './components/producto-form/producto-form.component';
import { ProductoDetailComponent } from './components/producto-detail/producto-detail.component';

type Vista = 'tabla' | 'alta' | 'edicion' | 'detalle';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [ProductoTableComponent, ProductoFormComponent, ProductoDetailComponent],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.scss',
})
export class ProductosComponent {
  readonly productoService = inject(ProductoService);

  readonly vista = signal<Vista>('tabla');

  irATabla() {
    this.productoService.limpiarSeleccion();
    this.vista.set('tabla');
  }

  irAAlta() { this.vista.set('alta'); }

  irAEdicion(producto: ProductoResponse) {
    this.productoService.seleccionar(producto);
    this.vista.set('edicion');
  }

  irADetalle(producto: ProductoResponse) {
    this.productoService.seleccionar(producto);
    this.vista.set('detalle');
  }

  onGuardado(esAlta: boolean) {
    if (esAlta) {
      this.productoService.filtroTipo.set(null);
    }
    this.irATabla();
  }
}