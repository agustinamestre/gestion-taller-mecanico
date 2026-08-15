import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import {
  ProductoResponse,
  ProductoRequest,
  ModificarProductoRequest,
  ActualizarPrecioRequest,
  ActualizarStockRequest,
  TipoProducto,
} from '../models/producto.model';

const API_BASE = `${environment.apiUrl}/productos`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private readonly http = inject(HttpClient);
  private readonly notification = inject(NotificationService);

  readonly productos = signal<ProductoResponse[]>([]);
  readonly productoSeleccionado = signal<ProductoResponse | null>(null);
  readonly filtroTipo = signal<TipoProducto | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);
  readonly mostrarInactivos = signal(false);

  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  readonly productosFiltrados = computed(() => {
    const tipo = this.filtroTipo();
    return this.productos().filter(p => (tipo ? p.tipo === tipo : true)
    );
  });

  listar() {
    this.estadoCarga.set('cargando');
    this.error.set(null);
    return this.http.get<ProductoResponse[]>(API_BASE).pipe(
      tap(productos => { this.productos.set(productos); this.estadoCarga.set('exito'); }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: ProductoRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);
    return this.http.post<ProductoResponse>(API_BASE, request).pipe(
      tap(nuevo => {
        this.productos.update(lista => [...lista, nuevo]);
        this.estadoCarga.set('exito');
        this.notification.exito('Producto registrado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificar(id: number, request: ModificarProductoRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);
    return this.http.put<ProductoResponse>(`${API_BASE}/${id}`, request).pipe(
      tap(actualizado => {
        this.productos.update(lista => lista.map(p => p.id === id ? actualizado : p));
        this.productoSeleccionado.set(actualizado);
        this.estadoCarga.set('exito');
        this.notification.exito('Producto modificado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  actualizarPrecio(id: number, request: ActualizarPrecioRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);
    return this.http.patch<ProductoResponse>(`${API_BASE}/${id}/precio`, request).pipe(
      tap(actualizado => {
        this.productos.update(lista => lista.map(p => p.id === id ? actualizado : p));
        this.productoSeleccionado.set(actualizado);
        this.estadoCarga.set('exito');
        this.notification.exito('Precio actualizado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  actualizarStock(id: number, request: ActualizarStockRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);
    return this.http.patch<ProductoResponse>(`${API_BASE}/${id}/stock`, request).pipe(
      tap(actualizado => {
        this.productos.update(lista => lista.map(p => p.id === id ? actualizado : p));
        this.productoSeleccionado.set(actualizado);
        this.estadoCarga.set('exito');
        this.notification.exito('Stock actualizado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  seleccionar(producto: ProductoResponse) { this.productoSeleccionado.set(producto); }

  limpiarSeleccion() { this.productoSeleccionado.set(null); }

  private manejarError(err: HttpErrorResponse) {
    const errores = err.error?.error;
    const mensaje = Array.isArray(errores) && errores.length > 0
      ? errores.map((e: any) => e.errorMessage).join(', ')
      : 'Error inesperado del servidor';

    this.error.set(mensaje);
    this.estadoCarga.set('error');
    this.notification.error(mensaje);
    return throwError(() => err);
  }
}