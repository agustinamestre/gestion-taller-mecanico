import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { NotificationService } from '../../../shared/services/notification.service';
import { MarcaResponse, MarcaRequest } from '../models/marca.model';
import { environment } from '../../../../environments/environment';

const API_BASE = `${environment.apiUrl}/marcas`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class MarcaService {
  private readonly http = inject(HttpClient);
  private readonly notification = inject(NotificationService);

  readonly marcas = signal<MarcaResponse[]>([]);
  readonly marcaSeleccionada = signal<MarcaResponse | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  readonly marcasActivas = computed(() => this.marcas().filter((m) => m.activo));
  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  listar() {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.get<MarcaResponse[]>(API_BASE).pipe(
      tap((marcas) => {
        this.marcas.set(marcas);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: MarcaRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.post<MarcaResponse>(API_BASE, request).pipe(
      tap((nueva) => {
        this.marcas.update((lista) => [...lista, nueva]);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificar(id: number, request: MarcaRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.put<MarcaResponse>(`${API_BASE}/${id}`, request).pipe(
      tap((actualizada) => {
        this.marcas.update((lista) =>
          lista.map((m) => (m.id === id ? actualizada : m))
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
        this.marcas.update((lista) =>
          lista.map((m) => (m.id === id ? { ...m, activo: false } : m))
        );
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.marcaSeleccionada.set(null);
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