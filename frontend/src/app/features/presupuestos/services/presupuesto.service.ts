import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import {
  PresupuestoResponse,
  PresupuestoSummaryResponse,
  PresupuestoRequest,
  ItemPresupuestoRequest,
  ModificarItemPresupuestoRequest,
  CambiarEstadoPresupuestoRequest,
} from '../models/presupuesto.model';

const API_BASE = `${environment.apiUrl}/presupuestos`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class PresupuestoService {
  private readonly http = inject(HttpClient);
  private readonly toast = inject(NotificationService);

  readonly listado = signal<PresupuestoSummaryResponse[]>([]);
  readonly seleccionado = signal<PresupuestoResponse | null>(null);
  readonly estado = signal<EstadoCarga>('idle');
  readonly mensajeError = signal<string | null>(null);

  readonly cargando = computed(() => this.estado() === 'cargando');

  listar() {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.get<PresupuestoSummaryResponse[]>(API_BASE).pipe(
      tap(lista => { this.listado.set(lista); this.estado.set('exito'); }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  buscarPorPatente(patente: string) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    const params = new HttpParams().set('patente', patente);
    return this.http.get<PresupuestoSummaryResponse[]>(API_BASE, { params }).pipe(
      tap(lista => { this.listado.set(lista); this.estado.set('exito'); }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  obtener(id: number) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.get<PresupuestoResponse>(`${API_BASE}/${id}`).pipe(
      tap(presupuesto => { this.seleccionado.set(presupuesto); this.estado.set('exito'); }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: PresupuestoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.post<PresupuestoResponse>(API_BASE, request).pipe(
      tap(nuevo => {
        this.seleccionado.set(nuevo);
        this.estado.set('exito');
        this.toast.exito('Presupuesto creado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  agregarItem(presupuestoId: number, request: ItemPresupuestoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.post<PresupuestoResponse>(`${API_BASE}/${presupuestoId}/items`, request).pipe(
      tap(actualizado => {
        this.seleccionado.set(actualizado);
        this.estado.set('exito');
        this.toast.exito('Ítem agregado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificarItem(presupuestoId: number, itemId: number, request: ModificarItemPresupuestoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.put<PresupuestoResponse>(`${API_BASE}/${presupuestoId}/items/${itemId}`, request).pipe(
      tap(actualizado => {
        this.seleccionado.set(actualizado);
        this.estado.set('exito');
        this.toast.exito('Ítem modificado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  eliminarItem(presupuestoId: number, itemId: number) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.delete<void>(`${API_BASE}/${presupuestoId}/items/${itemId}`).pipe(
      tap(() => {
        this.estado.set('exito');
        this.toast.exito('Ítem eliminado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  cambiarEstado(presupuestoId: number, request: CambiarEstadoPresupuestoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.patch<void>(`${API_BASE}/${presupuestoId}/estado`, request).pipe(
      tap(() => {
        this.estado.set('exito');
        this.toast.exito('Estado actualizado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.seleccionado.set(null);
  }

  limpiarBusqueda() {
    this.listado.set([]);
  }

  private manejarError(err: HttpErrorResponse) {
    const errores = err.error?.error;
    const mensaje = Array.isArray(errores) && errores.length > 0
      ? errores.map((e: any) => e.errorMessage).join(', ')
      : 'Error inesperado del servidor';

    this.mensajeError.set(mensaje);
    this.estado.set('error');
    this.toast.error(mensaje);
    return throwError(() => err);
  }
}