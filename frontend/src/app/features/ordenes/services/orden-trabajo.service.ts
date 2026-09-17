import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import {
  OrdenTrabajoResponse,
  RegistrarOrdenTrabajoRequest,
  ModificarOrdenTrabajoRequest,
  AgregarItemOrdenTrabajoRequest,
  ModificarItemOrdenTrabajoRequest,
  CambiarEstadoOrdenTrabajoRequest,
  ItemOrdenTrabajoResponse,
  EstadoOrdenTrabajo,
} from '../models/orden-trabajo.model';

const API_BASE = `${environment.apiUrl}/ordenes`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class OrdenTrabajoService {
  private readonly http = inject(HttpClient);
  private readonly toast = inject(NotificationService);

  readonly listado = signal<OrdenTrabajoResponse[]>([]);
  readonly seleccionado = signal<OrdenTrabajoResponse | null>(null);
  readonly estado = signal<EstadoCarga>('idle');
  readonly mensajeError = signal<string | null>(null);
  readonly itemEnEdicion = signal<ItemOrdenTrabajoResponse | null>(null);

  readonly cargando = computed(() => this.estado() === 'cargando');

  listar(filtros?: { patente?: string; estado?: EstadoOrdenTrabajo }) {
    this.estado.set('cargando');
    this.mensajeError.set(null);

    let params = new HttpParams();
    if (filtros?.patente) params = params.set('patente', filtros.patente);
    if (filtros?.estado) params = params.set('estado', filtros.estado);

    return this.http.get<OrdenTrabajoResponse[]>(API_BASE, { params }).pipe(
      tap(lista => { this.listado.set(lista); this.estado.set('exito'); }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  obtener(id: number) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.get<OrdenTrabajoResponse>(`${API_BASE}/${id}`).pipe(
      tap(orden => { this.seleccionado.set(orden); this.estado.set('exito'); }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: RegistrarOrdenTrabajoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.post<OrdenTrabajoResponse>(API_BASE, request).pipe(
      tap(nueva => {
        this.seleccionado.set(nueva);
        this.estado.set('exito');
        this.toast.exito('Orden de trabajo creada correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificar(id: number, request: ModificarOrdenTrabajoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.put<OrdenTrabajoResponse>(`${API_BASE}/${id}`, request).pipe(
      tap(actualizada => {
        this.seleccionado.set(actualizada);
        this.estado.set('exito');
        this.toast.exito('Orden modificada correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  seleccionarItem(item: ItemOrdenTrabajoResponse | null) {
    this.itemEnEdicion.set(item);
  }

  agregarItem(ordenId: number, request: AgregarItemOrdenTrabajoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.post<OrdenTrabajoResponse>(`${API_BASE}/${ordenId}/items`, request).pipe(
      tap(actualizada => {
        this.seleccionado.set(actualizada);
        this.estado.set('exito');
        this.toast.exito('Ítem agregado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificarItem(ordenId: number, itemId: number, request: ModificarItemOrdenTrabajoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.put<OrdenTrabajoResponse>(`${API_BASE}/${ordenId}/items/${itemId}`, request).pipe(
      tap(actualizada => {
        this.seleccionado.set(actualizada);
        this.estado.set('exito');
        this.toast.exito('Ítem modificado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  eliminarItem(ordenId: number, itemId: number) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.delete<void>(`${API_BASE}/${ordenId}/items/${itemId}`).pipe(
      tap(() => {
        this.estado.set('exito');
        this.toast.exito('Ítem eliminado correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  cambiarEstado(ordenId: number, request: CambiarEstadoOrdenTrabajoRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);
    return this.http.patch<void>(`${API_BASE}/${ordenId}/estado`, request).pipe(
      tap(() => {
        this.estado.set('exito');
        this.toast.exito('Estado actualizado correctamente');
        this.listado.update(lista =>
          lista.map(o => o.id === ordenId ? { ...o, estado: request.nuevoEstado } : o)
        );
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