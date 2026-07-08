import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import { ModeloResponse, ModeloRequest } from '../models/modelo.model';

const API_BASE = `${environment.apiUrl}/modelos`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class ModeloService {
  private readonly http = inject(HttpClient);
  private readonly notification = inject(NotificationService);

  readonly modelos = signal<ModeloResponse[]>([]);
  readonly modeloSeleccionado = signal<ModeloResponse | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  readonly modelosActivos = computed(() => this.modelos().filter((m) => m.activo));
  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  listar(marcaId?: number) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    let params = new HttpParams();
    if (marcaId != null) {
      params = params.set('marcaId', marcaId);
    }

    return this.http.get<ModeloResponse[]>(API_BASE, { params }).pipe(
      tap((modelos) => {
        this.modelos.set(modelos);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: ModeloRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.post<ModeloResponse>(API_BASE, request).pipe(
      tap((nuevo) => {
        this.modelos.update((lista) => [...lista, nuevo]);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificar(id: number, request: ModeloRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.put<ModeloResponse>(`${API_BASE}/${id}`, request).pipe(
      tap((actualizado) => {
        this.modelos.update((lista) =>
          lista.map((m) => (m.id === id ? actualizado : m))
        );
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  desactivar(id: number) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.delete<void>(`${API_BASE}/${id}`).pipe(
      tap(() => {
        this.modelos.update((lista) =>
          lista.map((m) => (m.id === id ? { ...m, activo: false } : m))
        );
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.modeloSeleccionado.set(null);
  }

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